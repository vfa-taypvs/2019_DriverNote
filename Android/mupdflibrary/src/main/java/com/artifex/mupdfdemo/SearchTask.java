package com.artifex.mupdfdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.os.Handler;
//import com.thelawstudio.luatdoannghiep.word;

class ProgressDialogX extends ProgressDialog {
    public ProgressDialogX(Context context) {
        super(context);
    }

    private boolean mCancelled = false;

    public boolean isCancelled() {
        return mCancelled;
    }

    @Override
    public void cancel() {
        mCancelled = true;
        super.cancel();
    }
}

public abstract class SearchTask {
    private static final int SEARCH_PROGRESS_DELAY = 200;
    private final Context mContext;
    private final MuPDFCore mCore;
    private final Handler mHandler;
    private final AlertDialog.Builder mAlertBuilder;
    private AsyncTask<Void, Integer, SearchTaskResult> mSearchTask;

    private final String vowels = "aeiouAEIOU";
    private final String consonants = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";

    public SearchTask(Context context, MuPDFCore core) {
        mContext = context;
        mCore = core;
        mHandler = new Handler();
        mAlertBuilder = new AlertDialog.Builder(context);
    }

    protected abstract void onTextFound(SearchTaskResult result);

    public void stop() {
        if (mSearchTask != null) {
            mSearchTask.cancel(true);
            mSearchTask = null;
        }
    }

    public void go(final String text, int direction, int displayPage, int searchPage) {
        if (mCore == null)
            return;
        stop();

        final int increment = direction;
        final int startIndex = searchPage == -1 ? displayPage : searchPage + increment;

        final ProgressDialogX progressDialog = new ProgressDialogX(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(mContext.getString(R.string.searching_));
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                stop();
            }
        });
        progressDialog.setMax(mCore.countPages());

        mSearchTask = new AsyncTask<Void, Integer, SearchTaskResult>() {
            @Override
            protected SearchTaskResult doInBackground(Void... params) {
                int index = startIndex;

                while (0 <= index && index < mCore.countPages() && !isCancelled()) {
                    publishProgress(index);
//                    RectF searchHits[] = mCore.searchPage(index, text);
//                    RectF searchHits[] = concatRectF(mCore.searchPage(index, text),mCore.searchPage(index, "a"));
                    RectF searchHits[] = getAllRectF(text,index);
                    if (searchHits != null && searchHits.length > 0)
                        return new SearchTaskResult(text, index, searchHits);

                    index += increment;
                }
                return null;
            }

            @Override
            protected void onPostExecute(SearchTaskResult result) {
                progressDialog.cancel();
                if (result != null) {
                    onTextFound(result);
                } else {
//                    mAlertBuilder.setTitle(SearchTaskResult.get() == null ? R.string.text_not_found : R.string.no_further_occurrences_found);
//                    AlertDialog alert = mAlertBuilder.create();
//                    alert.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getString(R.string.dismiss),
//                            (DialogInterface.OnClickListener) null);
//                    alert.show();
                }
            }

            @Override
            protected void onCancelled() {
                progressDialog.cancel();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressDialog.setProgress(values[0].intValue());
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (!progressDialog.isCancelled()) {
                            if(!text.equals("non-words-here"))
                                progressDialog.show();
                            progressDialog.setProgress(startIndex);
                        }
                    }
                }, SEARCH_PROGRESS_DELAY);
            }
        };

        mSearchTask.execute();
    }

    private RectF[] concatRectF(RectF[] A, RectF[] B) {
        int aLen = A.length;
        int bLen = B.length;
        RectF[] C= new RectF[aLen+bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    private RectF[] getAllRectF(String strInput, int index){
        if(strInput.equals("non-words-here"))
            return mCore.searchPage(index, strInput);
        String[] strAfterSplit = collectCutStrings(strInput);
        if(strAfterSplit!=null) {
            RectF[] finalRectf = concatRectF(mCore.searchPage(index, strInput), mCore.searchPage(index, strAfterSplit[0]));
            if (strAfterSplit.length > 1) {
                for (int i = 1; i < strAfterSplit.length; i++) {
                    finalRectf = concatRectF(finalRectf, mCore.searchPage(index, strAfterSplit[i]));
                }
            }
            return finalRectf;
        }
        else
            return mCore.searchPage(index, strInput);
    }

    private String[] getWordsFromString(String strInput) {
        return strInput.trim().split("\\s+");
    }

    private String removeVowels(String inputStr) {
        for (int i = 0; i < inputStr.length(); i++) {
            String curChar = String.valueOf(inputStr.charAt(i));
            if (isVowel(curChar)) {  // If is consonant - keep and add to String
                inputStr = inputStr.replace(curChar, " ");
            }
        }
        return inputStr;
    }

    private String[] collectCutStrings(String strInput){
        return getWordsFromString(removeVowels(strInput));
    }

    private boolean isVowel(String c){
        return vowels.contains(c);
    }


}
