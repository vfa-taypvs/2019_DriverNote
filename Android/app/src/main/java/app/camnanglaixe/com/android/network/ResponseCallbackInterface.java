package app.camnanglaixe.com.android.network;

/**
 * Created by phamvietsontay on 11/27/16.
 */
public interface ResponseCallbackInterface {
    public void onResultSuccess(Object result, String TAG);
    public void onResultFail(Object resultFail, String TAG);

}
