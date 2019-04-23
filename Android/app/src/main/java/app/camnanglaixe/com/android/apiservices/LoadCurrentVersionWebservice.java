package app.camnanglaixe.com.android.apiservices;

import android.content.Context;

import com.android.volley.toolbox.Volley;

import app.camnanglaixe.com.android.Common.Constanst;
import app.camnanglaixe.com.android.network.HttpVolleyConnector;
import app.camnanglaixe.com.android.network.ResponseCallbackInterface;

/**
 * Created by phamvietsontay on 4/6/18.
 */

public class LoadCurrentVersionWebservice  extends HttpVolleyConnector {

    public LoadCurrentVersionWebservice(ResponseCallbackInterface respone, Context context){
        mContext = context;
        url = Constanst.API_GET_CURRENT_VERSION;
        responeCallback = respone;
        mRequestQueue =  Volley.newRequestQueue(mContext.getApplicationContext());
    }

    public void doLoadAPI(){
        doConnectingApi(Constanst.GET, Constanst.TAG_API_GET_CURRENT_VERSION, RETURN_OBJECT_JSON);
    }
}
