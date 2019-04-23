package app.camnanglaixe.com.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.AsyncTask;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.Common.Constanst;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.jsonhandler.JsonParseMachine;
import app.camnanglaixe.com.android.models.SubTopicObject;

/**
 * Created by taypham on 16/12/2016.
 */
public class ContentDetailPDFActivity extends BaseActivity {
    private RelativeLayout pdfView;
    private SubTopicObject currentSubTopic;
    private MuPDFCore core;
    private MuPDFReaderView mDocview;
    private String mFilePath;
    private String linkPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_pdf);

        init();
    }

    protected void init() {
        setBackBtnOnclick();
        if (getIntent().hasExtra("KEY_CONTENT")) {
            try {
                String json = getIntent().getStringExtra("KEY_CONTENT");
                JSONObject jsonObject = new JSONObject(json);
                currentSubTopic = JsonParseMachine.parseSubTopic(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ((TextView) findViewById(R.id.title)).setText(currentSubTopic.title);
        pdfView = (RelativeLayout) findViewById(R.id.content_pdf_view);

        if(currentSubTopic.content.size()>0) {
            mFilePath = CommonUtils.createFilePath(currentSubTopic.content.get(0).image + ".pdf", Constanst.FILE_DRIVER_DOWNLOAD_MAIN_PDF, true);
            File pdfFile = new File(mFilePath);
            if (!pdfFile.exists()) {
                new copyPdfFromAsset().execute();
            } else {
                Log.d("TayPVS", "TayPVS - mFilePath " + mFilePath);
                core = openFile(CommonUtils.createFilePath(currentSubTopic.content.get(0).image + ".pdf", Constanst.FILE_DRIVER_DOWNLOAD_MAIN_PDF, true));
                if (core != null && core.countPages() == 0) {
                    core = null;
                }
                if (core == null || core.countPages() == 0 || core.countPages() == -1) {
                    Log.d("TayPVS", " TayPVSDocument Not Opening");
                    return;
                }
                if (core != null) {
                    mDocview = new MuPDFReaderView(this) {
                        @Override
                        protected void onMoveToChild(int i) {
                            if (core == null)
                                return;
                            super.onMoveToChild(i);
                        }

                    };

                    mDocview.setAdapter(new MuPDFPageAdapter(this, null, core));
                    pdfView.addView(mDocview);
                    findViewById(R.id.print_btn).setVisibility(View.VISIBLE);
                    findViewById(R.id.print_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
                                CommonUtils.printPDFFile(getBaseContext(), currentSubTopic.content.get(0).image + ".pdf");
                            } else{
                                showDialogSdkError();
                            }

                        }
                    });
                }
            }

        }

    }

    public void onDestroy() {
        super.onDestroy();
    }

    private MuPDFCore openFile(String path) {
        try {
            core = new MuPDFCore(getBaseContext(), path);
            // New file: drop the old outline data
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return core;
    }

    private class copyPdfFromAsset extends AsyncTask<String, String, String> {
        ProgressDialog processDialog;
        int count;
        InputStream input;
        OutputStream output = null;
        File mPdfFile;
        String filePath;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            processDialog = new ProgressDialog(ContentDetailPDFActivity.this);
//            processDialog.setMessage("Processing...");
//            processDialog.show();

        }


        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            try {
                mPdfFile = new File(CommonUtils.createFilePath(currentSubTopic.content.get(0).image+".pdf", Constanst.FILE_DRIVER_DOWNLOAD_MAIN_PDF, false));
                mPdfFile.mkdirs();
                try {
                    InputStream is = getAssets().open(currentSubTopic.content.get(0).image + ".pdf");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();


                    FileOutputStream fos = new FileOutputStream(CommonUtils.createFilePath(currentSubTopic.content.get(0).image+".pdf", Constanst.FILE_DRIVER_DOWNLOAD_MAIN_PDF, true));
                    fos.write(buffer);
                    fos.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

            return "";
        }

        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String file_url) {
//            if (processDialog.isShowing())
//                processDialog.dismiss();
            cancel(false);
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            try
            {
                Gson gson = new Gson();
                String json = gson.toJson(currentSubTopic);
                startContentActivity(Constanst.TYPE_POST_4, json);
                finish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void showDialogSdkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContentDetailPDFActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Cẩm Nang Người Lái Xe");
        builder.setMessage("Chức năng này chỉ dành cho Android 4.4 trở lên");
        builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //DO TASK
                arg0.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        //After calling show method, you need to check your condition and
        //enable/ disable buttons of dialog
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false); //BUTTON1 is positive button
    }
}
