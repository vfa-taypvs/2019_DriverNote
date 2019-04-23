package app.camnanglaixe.com.android.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.Common.Constanst;
import app.camnanglaixe.com.android.Common.PreferenceUtils;
import app.camnanglaixe.com.android.Common.VersionChecker;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.apiservices.LoadCurrentVersionWebservice;
import app.camnanglaixe.com.android.apiservices.LoadFullJsonWebservice;
import app.camnanglaixe.com.android.models.FullTopics;
import app.camnanglaixe.com.android.models.Version;
import app.camnanglaixe.com.android.network.ResponseCallbackInterface;

/**
 * Created by taypham on 30/11/2016.
 */
public class SplashActivity extends BaseActivity implements ResponseCallbackInterface {

    LoadFullJsonWebservice loadAllws;
    LoadCurrentVersionWebservice loadVestionws;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (checkIfAlreadyhavePermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        } else {
            init();
        }

//       new LoadFileJsonTask().execute("");

    }

    protected void init() {

        if (PreferenceUtils.isFirstTimeLaungh(getBaseContext())) {
//            CommonUtils.clearPreferencesTopics(getBaseContext());

            CommonUtils.clearFolderApp();
            if (CommonUtils.isOnline(getBaseContext())) {
                loadVestionws = new LoadCurrentVersionWebservice(this, this);
                loadVestionws.doLoadAPI();

            } else {
                if(CommonUtils.isSavedTopics(getBaseContext())){
                    // Execute some code after 2 seconds have passed
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }, 3000);

                }
                else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new LoadFileJsonTask().execute("");
                        }
                    }, 3000);


                }
            }
        } else {
//          PreferenceUtils.saveFirstTimeLaungh(getBaseContext());
        }
    }

    @Override
    public void onResultSuccess(Object result, String TAG) {
        switch (TAG) {
            case Constanst.TAG_API_GET_FULL_INFO:
//                ((TextView) findViewById(R.id.test)).setText(result.toString());
                // Save JSON from server to Internal Files
                FullTopics fullTopics = new FullTopics(getBaseContext(), (JSONArray)result);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }, 3000);
                finish();
                break;
            case Constanst.TAG_API_GET_CURRENT_VERSION:
//                ((TextView) findViewById(R.id.test)).setText(result.toString());
                // Save JSON from server to Internal Files
                Version version = new Version((JSONObject)result);

                String latestVersion = version.value;

                if (CommonUtils.isHaveNewVersion(latestVersion, CommonUtils.getAppVersion(getBaseContext()))) {
                    showDialogUpdate();
                } else {
                    loadAllws = new LoadFullJsonWebservice(this, this);
                    loadAllws.doLoadAPI();
                }

                break;
        }
    }

    @Override
    public void onResultFail(Object resultFail, String TAG) {
        Log.d("TayPVS", "TayPVS - onResultFail : " + resultFail.toString());
        switch (TAG) {
            case Constanst.TAG_API_GET_FULL_INFO:
                new LoadFileJsonTask().execute("");
                break;
            case Constanst.TAG_API_GET_CURRENT_VERSION:
                loadAllws = new LoadFullJsonWebservice(this, this);
                loadAllws.doLoadAPI();
                break;
        }
    }

    // Load File From JSON TEST
    private class LoadFileJsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // we use the OkHttp library from https://github.com/square/okhttp
            try {
                // Load JSon From Asset
                JSONArray jsonObject = new JSONArray(CommonUtils.loadJSONFromAsset(getBaseContext(), Constanst.FILE_JSON_TEST));
                FullTopics testTopic = new FullTopics(getBaseContext(), jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }

    }

    public void showDialogUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Cẩm Nang Người Lái Xe");
        builder.setMessage("Đã có bản cập nhật mới trên Google Play!!!");
        builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg0 != null) {
                    arg0.dismiss();
                }
                //DO TASK
                CommonUtils.openAppRating(getBaseContext());
                System.exit(0);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        //After calling show method, you need to check your condition and
        //enable/ disable buttons of dialog
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false); //BUTTON1 is positive button
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);
                } else {
                    //not granted
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    init ();
                } else {
                    //not granted
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean checkIfAlreadyhavePermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}


