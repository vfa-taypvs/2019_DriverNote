package app.camnanglaixe.com.android.models;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by phamvietsontay on 4/6/18.
 */

public class Version {
    public String meta_key;
    public String value;

    public Version(JSONObject jsonObject) {
        this.meta_key = jsonObject.optString("meta_key","");
        this.value = jsonObject.optString("value","");
    }

}
