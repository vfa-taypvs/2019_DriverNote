package app.camnanglaixe.com.android.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.Common.PreferenceUtils;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.adapter.ListSubTopicAdapter;
import app.camnanglaixe.com.android.jsonhandler.JsonParseMachine;
import app.camnanglaixe.com.android.models.SubTopicObject;
import app.camnanglaixe.com.android.models.Topic;

/**
 * Created by taypham on 05/12/2016.
 */
public class ListSubTopicActivity extends BaseActivity {

    private ListView listViewSTopic;
    private Topic currentTopic;
    List<SubTopicObject> subTopics;
    private ListSubTopicAdapter listSubTopicAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic);
        init();
    }

    protected void init(){
        setBackBtnOnclick();
        if (getIntent().hasExtra("KEY_TOPIC")) {
            try {
                int i = getIntent().getIntExtra("KEY_TOPIC", 0);
                JSONObject jsonObject = new JSONObject(PreferenceUtils.getString(getBaseContext(), PreferenceUtils.TOPIC_NUMBER + i));
                currentTopic = JsonParseMachine.parseTopic(jsonObject);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        ((TextView)findViewById(R.id.title)).setText(currentTopic.name);

        listViewSTopic = (ListView) findViewById(R.id.listSubTopic);
        if (getIntent().hasExtra("LOI_PHAT")) {
            if(getIntent().getBooleanExtra("LOI_PHAT", false))
                subTopics = currentTopic.small_loiPhat;
            else
                subTopics = currentTopic.small_topic;
            Log.d("Tag", "TayPVS - subTopicTitle : LOI_PHAT " + getIntent().getBooleanExtra("LOI_PHAT", false) );
        }
        else {
            subTopics = currentTopic.small_topic;
        }
        listSubTopicAdapter = new ListSubTopicAdapter(getBaseContext(), subTopics);
        listViewSTopic.setAdapter(listSubTopicAdapter);
        listViewSTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Convert topic to String and save
                Gson gson = new Gson();
                String json = gson.toJson(subTopics.get(i));
                startContentActivity(subTopics.get(i).type_name, json);
//                overridePendingTransition(R.anim.slide_from_right, 0);
            }
        });

        final AdView mAdView = (AdView) findViewById(R.id.adView);
        if (CommonUtils.isOnline(getBaseContext())) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(CommonUtils.getDeviceId(getBaseContext()))
                    .build();
            mAdView.loadAd(adRequest);
        }
        else{
            mAdView.setVisibility(View.GONE);
        }
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                mAdView.setVisibility(View.GONE);
            }
        });
    }
}
