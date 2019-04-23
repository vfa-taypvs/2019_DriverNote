package app.camnanglaixe.com.android.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import app.camnanglaixe.com.android.Common.Constanst;

/**
 * Created by taypham on 29/11/2016.
 */
public class Topic {
    public String id;
    public String name;
    public String type_name;
    public String icon;
    public String version;
    public List<SubTopicObject> small_topic;
    public List<SubTopicObject> small_topicND;
    public List<SubTopicObject> small_thamQuyen;
    public List<SubTopicObject> small_loiPhat;
    public List<SubTopicObject> small_thongtu01;

    public Topic(String id, String type_name, String name, String icon, String version, JSONArray subTitleArray){
        this.id = id;
        this.name = name;
        this.type_name = type_name;
        this.icon = icon;
        this.version = version;
        small_topic = new ArrayList<SubTopicObject>();
        small_topicND = new ArrayList<SubTopicObject>();
        small_thamQuyen = new ArrayList<SubTopicObject>();
        small_loiPhat = new ArrayList<SubTopicObject>();
        small_thongtu01 = new ArrayList<SubTopicObject>();
        try {
            for (int i = 0; i < subTitleArray.length(); i++) {
                String subTopicId = subTitleArray.getJSONObject(i).optString("id");
                String subTopicTitle = subTitleArray.getJSONObject(i).optString("title");
                String subType = subTitleArray.getJSONObject(i).optString("type_name");
                String category_id = subTitleArray.getJSONObject(i).optString("category_id", "");
                JSONArray contentArray = subTitleArray.getJSONObject(i).optJSONArray("content");
                SubTopicObject newSubTopic = new SubTopicObject(subTopicId, subTopicTitle, subType, category_id,contentArray);
                if(name.toLowerCase().contains("xử phạt")){
                    if(subTopicTitle.contains("46/2016")||subTopicTitle.contains("46 /2016"))
                        small_topicND.add(newSubTopic);
                    else if (subTopicTitle.toLowerCase().equals("thẩm quyền xử phạt")) {
                        small_thamQuyen.add(newSubTopic);
                    }
                    else if (subTopicTitle.toLowerCase().contains("các lỗi xử phạt")) {
                        Log.d("Tag", "TayPVS - subTopicTitle : " + subTopicTitle );
                        small_loiPhat.add(newSubTopic);
                    }
                    else if (subTopicTitle.equals("THÔNG TƯ 01/2016/TT-BCA")) {
                        Log.d("Tag", "TayPVS - subTopicTitle : " + subTopicTitle );
                        small_thongtu01.add(newSubTopic);
                    }
                    else
                        small_topic.add(newSubTopic);
                }
                else {
                    small_topic.add(newSubTopic);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public Topic () {

    }

    public void addSubtopicND(JSONArray subTitleArray){
        try {
            for (int i = 0; i < subTitleArray.length(); i++) {
                String subTopicId = subTitleArray.getJSONObject(i).optString("id");
                String subTopicTitle = subTitleArray.getJSONObject(i).optString("title");
                String subType = subTitleArray.getJSONObject(i).optString("type_name");
                String category_id = subTitleArray.getJSONObject(i).optString("category_id", "");
                JSONArray contentArray = subTitleArray.getJSONObject(i).optJSONArray("content");
                SubTopicObject newSubTopic = new SubTopicObject(subTopicId, subTopicTitle, subType, category_id,contentArray);
                small_topicND.add(newSubTopic);
            }
        }catch (JSONException e){

        }
    }

    public void addSubtopicTQ(JSONArray subTitleArray){
        try {
            for (int i = 0; i < subTitleArray.length(); i++) {
                String subTopicId = subTitleArray.getJSONObject(i).optString("id");
                String subTopicTitle = subTitleArray.getJSONObject(i).optString("title");
                String subType = subTitleArray.getJSONObject(i).optString("type_name");
                String category_id = subTitleArray.getJSONObject(i).optString("category_id", "");
                JSONArray contentArray = subTitleArray.getJSONObject(i).optJSONArray("content");
                SubTopicObject newSubTopic = new SubTopicObject(subTopicId, subTopicTitle, subType, category_id,contentArray);
                small_thamQuyen.add(newSubTopic);
            }
        }catch (JSONException e){

        }
    }

    public void addSubtopicTT(JSONArray subTitleArray){
        try {
            for (int i = 0; i < subTitleArray.length(); i++) {
                String subTopicId = subTitleArray.getJSONObject(i).optString("id");
                String subTopicTitle = subTitleArray.getJSONObject(i).optString("title");
                String subType = subTitleArray.getJSONObject(i).optString("type_name");
                String category_id = subTitleArray.getJSONObject(i).optString("category_id", "");
                JSONArray contentArray = subTitleArray.getJSONObject(i).optJSONArray("content");
                SubTopicObject newSubTopic = new SubTopicObject(subTopicId, subTopicTitle, subType, category_id,contentArray);
                small_thongtu01.add(newSubTopic);
            }
        }catch (JSONException e){

        }
    }

    public void addSubtopicLP (JSONArray subTitleArray){
        try {
            for (int i = 0; i < subTitleArray.length(); i++) {
                String subTopicId = subTitleArray.getJSONObject(i).optString("id");
                String subTopicTitle = subTitleArray.getJSONObject(i).optString("title");
                String subType = subTitleArray.getJSONObject(i).optString("type_name");
                String category_id = subTitleArray.getJSONObject(i).optString("category_id", "");
                JSONArray contentArray = subTitleArray.getJSONObject(i).optJSONArray("content");
                SubTopicObject newSubTopic = new SubTopicObject(subTopicId, subTopicTitle, subType, category_id,contentArray);
                small_loiPhat.add(newSubTopic);
            }
        }catch (JSONException e){

        }
    }
}
