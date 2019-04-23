package app.camnanglaixe.com.android.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GroupContentDetailRule {
    public List<ContentDetailRule> contents;
    public String title;

    public GroupContentDetailRule () {

    }

    public GroupContentDetailRule (JSONArray contentArray) {
//        this.title = title;
        contents = new ArrayList<ContentDetailRule>();
        GroupContentDetailRule groupContent = null;
        try {
            for (int i = 0; i < contentArray.length(); i++) {
                String contentDetail = contentArray.getJSONObject(i).optString("detail");
                String contentTitle = contentArray.getJSONObject(i).optString("title");
                String contentImage = contentArray.getJSONObject(i).optString("image");
                ContentDetailRule contentDetailRule = new ContentDetailRule(contentTitle, contentDetail, contentImage);
                contents.add(contentDetailRule);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void generate() {
        contents = new ArrayList<ContentDetailRule>();
    }

    public void addContent (ContentDetailRule newContent) {
        if (contents == null)
            generate();
        contents.add(newContent);
    }

    public List<ContentDetailRule> getList () {
        if (contents == null)
            generate();
        return contents;
    }
}
