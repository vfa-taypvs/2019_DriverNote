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

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.Common.PreferenceUtils;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.adapter.ListContentAdapter;
import app.camnanglaixe.com.android.adapter.ListContentGroupAdapter;
import app.camnanglaixe.com.android.jsonhandler.JsonParseMachine;
import app.camnanglaixe.com.android.models.SubTopicObject;

/**
 * Created by taypham on 06/12/2016.
 */
public class ContentDetailListActivity extends BaseActivity {

    // Contents
    private ListView contentListview;
    private ListContentAdapter listContentAdapter;

    // GroupContents
    private ListView groupContentListview;
    private ListContentGroupAdapter listGroupContentAdapter;

    private SubTopicObject currentSubTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_text);

        init();
    }

    protected void init(){
        setBackBtnOnclick();
        try {
            String json = PreferenceUtils.getString(getBaseContext(), PreferenceUtils.CONTENT_DETAIL);
            JSONObject jsonObject = new JSONObject(json);
            currentSubTopic = JsonParseMachine.parseSubTopic(jsonObject);
//            PreferenceUtils.clearKeyPreferences(getBaseContext(), PreferenceUtils.CONTENT_DETAIL);
        }catch (JSONException e){
            e.printStackTrace();
        }

        if (currentSubTopic!=null) {
            ((TextView)findViewById(R.id.title)).setText(currentSubTopic.title);
        }


        generateListContents ();
//        generateListGroupContents ();

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

    private void generateListContents () {
        contentListview = (ListView)findViewById(R.id.listContentText);
        listContentAdapter = new ListContentAdapter(getBaseContext(), currentSubTopic.content);
        contentListview.setAdapter(listContentAdapter);
        listContentAdapter.notifyDataSetChanged();
        contentListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gson gson = new Gson();
                String json = gson.toJson(currentSubTopic.content.get(i));
                startContentAdvance(json);
            }
        });
    }

    private void generateListGroupContents () {
        groupContentListview = (ListView)findViewById(R.id.listGroupContentText);
        if (currentSubTopic.groupContents.size() == 0) {
            groupContentListview.setVisibility(View.GONE);
        } else {
            groupContentListview.setVisibility(View.VISIBLE);
            listGroupContentAdapter = new ListContentGroupAdapter(getBaseContext(), currentSubTopic.groupContents);
            groupContentListview.setAdapter(listGroupContentAdapter);
            listGroupContentAdapter.notifyDataSetChanged();
            groupContentListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Gson gson = new Gson();
                    String json = gson.toJson(currentSubTopic.groupContents.get(i));
                    Log.d("TayPVS", "TayPVS - JSON Group : " + json);
                    startContentAdvance(json);
                }
            });
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
