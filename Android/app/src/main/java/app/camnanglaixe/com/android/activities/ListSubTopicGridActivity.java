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

import java.util.ArrayList;
import java.util.List;

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.Common.PreferenceUtils;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.adapter.ListSubTopicGridAdapter;
import app.camnanglaixe.com.android.jsonhandler.JsonParseMachine;
import app.camnanglaixe.com.android.models.Topic;

/**
 * Created by taypham on 20/12/2016.
 */
public class ListSubTopicGridActivity extends BaseActivity {

    private Topic currentTopic;
    private ListView listTopic;
    private ListSubTopicGridAdapter listSubTopicGridAdapter;
    int key_topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic_grid);
        init();
    }

    protected void init(){
        setBackBtnOnclick();
        if (getIntent().hasExtra("KEY_TOPIC")) {
            try {
                key_topic = getIntent().getIntExtra("KEY_TOPIC", 0);
                JSONObject jsonObject = new JSONObject(PreferenceUtils.getString(getBaseContext(), PreferenceUtils.TOPIC_NUMBER + key_topic));
                currentTopic = JsonParseMachine.parseTopic(jsonObject);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        List<String> listTopicStr = new ArrayList<String>();
        listTopicStr.add(currentTopic.small_topicND.get(0).title);
        listTopicStr.add(getResources().getString(R.string.mot_so_dieu_phat));
        listTopicStr.add(getResources().getString(R.string.tham_quyen_xu_phat));
        listTopicStr.add(currentTopic.small_thongtu01.get(0).title);
       // listTopicStr.add(getResources().getString(R.string.mot_so_loi_phat));

        ((TextView)findViewById(R.id.title)).setText(currentTopic.name);
        listTopic = (ListView)findViewById(R.id.listSubTopic);
        listSubTopicGridAdapter = new ListSubTopicGridAdapter(getBaseContext(), listTopicStr);
        listTopic.setAdapter(listSubTopicGridAdapter);
        listTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) {
                    Gson gson = new Gson();
                    String json = gson.toJson(currentTopic.small_topicND.get(0));
                    startContentActivity(currentTopic.small_topicND.get(0).type_name, json);
                }
                else if (i==2){
                    Gson gson = new Gson();
                    String json = gson.toJson(currentTopic.small_thamQuyen.get(0));
                    startContentActivity(currentTopic.small_thamQuyen.get(0).type_name, json);
                }
                else if (i==4){
                    Gson gson = new Gson();
                    String json = gson.toJson(currentTopic.small_loiPhat.get(0));
                    startContentActivity(currentTopic.small_loiPhat.get(0).type_name, json);
                }
                else if (i == 3) {
                    Gson gson = new Gson();
                    String json = gson.toJson(currentTopic.small_thongtu01.get(0));
                    startContentActivity(currentTopic.small_thongtu01.get(0).type_name, json);
                }
                else
                    startSubActivity(currentTopic.type_name, false, key_topic );
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
