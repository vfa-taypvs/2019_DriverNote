package app.camnanglaixe.com.android.jsonhandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.camnanglaixe.com.android.models.ContentDetailRule;
import app.camnanglaixe.com.android.models.GroupContentDetailRule;
import app.camnanglaixe.com.android.models.SubTopicObject;
import app.camnanglaixe.com.android.models.Topic;

/**
 * Created by taypham on 29/11/2016.
 */
public class JsonParseMachine {


//    public static JSONObject parseFile(Context context, String fileAssetName){
//        try {
//            JSONParser jsonParser = new JSONParser();
//            AssetFileDescriptor descriptor = context.getAssets().openFd(fileAssetName);
//            FileReader reader = new FileReader(descriptor.getFileDescriptor());
//            JSONObject jsonObject = (JSONObject)jsonParser.parse(reader);
//            return jsonObject;
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static Topic parseTopic(JSONObject jsonObject){
        Topic newTopic;
        String id = jsonObject.optString("id","");
        String version = jsonObject.optString("version", "");
        String name = jsonObject.optString("name","");
        String icon = jsonObject.optString("icon","");
        String type_name = jsonObject.optString("type_name","");
        newTopic = new Topic(id, type_name, name, icon, version, jsonObject.optJSONArray("small_topic"));
        try {
            if (jsonObject.getJSONArray("small_topicND") != null)
                newTopic.addSubtopicND(jsonObject.optJSONArray("small_topicND"));
            if (jsonObject.getJSONArray("small_thamQuyen") != null)
                newTopic.addSubtopicTQ(jsonObject.optJSONArray("small_thamQuyen"));
            if (jsonObject.getJSONArray("small_loiPhat") != null)
                newTopic.addSubtopicLP(jsonObject.optJSONArray("small_loiPhat"));
            if (jsonObject.getJSONArray("small_thongtu01") != null)
                newTopic.addSubtopicTT(jsonObject.optJSONArray("small_thongtu01"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return newTopic;
    }

    public static SubTopicObject parseSubTopic(JSONObject jsonObject){
        String id = jsonObject.optString("id","");
        String title = jsonObject.optString("title","");
        String type_name = jsonObject.optString("type_name","");
        String category_id = jsonObject.optString("category_id", "");
        JSONArray contentArray = jsonObject.optJSONArray("content");
        SubTopicObject newSubTopic = new SubTopicObject(id, title, type_name, category_id, contentArray);

        return newSubTopic;
    }

    public static ContentDetailRule parseContent(JSONObject jsonObject){
        String title = jsonObject.optString("title","");
        String detail = jsonObject.optString("detail","");
        String image = jsonObject.optString("image", "");
        ContentDetailRule newContent = new ContentDetailRule(title, detail, image);

        return newContent;
    }

    public static GroupContentDetailRule parseGroupContentDetail(JSONObject jsonObject){
        JSONArray contentArray = jsonObject.optJSONArray("content");
        GroupContentDetailRule newSubTopic = new GroupContentDetailRule(contentArray);

        return newSubTopic;
    }
}
