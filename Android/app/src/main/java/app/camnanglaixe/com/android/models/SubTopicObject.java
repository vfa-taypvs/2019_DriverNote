package app.camnanglaixe.com.android.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taypham on 29/11/2016.
 */
public class SubTopicObject {
    public String id;
    public String title;
    public String type_name;
    public String category_id;
    public List<ContentDetailRule> content;
    public List<GroupContentDetailRule> groupContents;


    public SubTopicObject(String id, String title, String type_name, String category_id, JSONArray contentArray){
        this.id = id;
        this.title = title;
        this.type_name = type_name;
        this.category_id = category_id;
        content = new ArrayList<ContentDetailRule>();
//        groupContents = new ArrayList<GroupContentDetailRule>();
//        GroupContentDetailRule groupContent = null;
        try {
            for (int i = 0; i < contentArray.length(); i++) {
                String contentDetail = contentArray.getJSONObject(i).optString("detail");
                String contentTitle = contentArray.getJSONObject(i).optString("title");
                String contentImage = contentArray.getJSONObject(i).optString("image");
                ContentDetailRule contentDetailRule = new ContentDetailRule(contentTitle, contentDetail, contentImage);
                content.add(contentDetailRule);
                // If title = last Title => Group 2 contents into 1,
                // add to GroupContentDetailRule
//                if (i!=0 && contentTitle.equals(content.get(i-1).title)){
//                    if (groupContent.getList().size() == 0) {
//                        groupContent.addContent(content.get(i-1));
//                    }
//                    groupContent.addContent(contentDetailRule);
//                    groupContent.title = contentDetailRule.title;
//                }
//                else {
//                    // Add Content to list Content => not a group
//                    content.add(contentDetailRule);
//
//                    // Check if have a Group Content => add to List
//                    if (groupContents !=null && groupContents.size()!=0) {
//                        groupContents.add(groupContent);
//                    }
//
//                    // If not a Group content || 1st Item, clear list and create new List
//                    groupContent = new GroupContentDetailRule();
//                    groupContent.generate();
//                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
