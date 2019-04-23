package app.camnanglaixe.com.android.apiservices;

import android.content.Context;

import com.android.volley.toolbox.Volley;

import app.camnanglaixe.com.android.Common.Constanst;
import app.camnanglaixe.com.android.network.HttpVolleyConnector;
import app.camnanglaixe.com.android.network.ResponseCallbackInterface;

/**
 * Created by phamvietsontay on 11/27/16.
 */
public class LoadFullJsonWebservice extends HttpVolleyConnector {


    public LoadFullJsonWebservice(ResponseCallbackInterface respone, Context context){
        mContext = context;
        url = Constanst.API_GET_FULL_INFO;
        responeCallback = respone;
        mRequestQueue =  Volley.newRequestQueue(mContext.getApplicationContext());
    }

    public void doLoadAPI(){
        doConnectingApi(Constanst.GET, Constanst.TAG_API_GET_FULL_INFO, RETURN_ARRAY_JSON);
    }

}
