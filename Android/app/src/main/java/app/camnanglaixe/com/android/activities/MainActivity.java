package app.camnanglaixe.com.android.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.Common.Constanst;
import app.camnanglaixe.com.android.Common.PreferenceUtils;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.adapter.ListTopicAdapter;
import app.camnanglaixe.com.android.jsonhandler.JsonParseMachine;
import app.camnanglaixe.com.android.models.Topic;


public class MainActivity extends BaseActivity{

    private ListTopicAdapter listTopicAdapter;
    private List<Topic> topics;
    private ListView topicGridView;
    private AdView mAdView;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.isInitialized();

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Success", "TayPVS - Success Login");
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://camnangnguoilaixe.com"))
                        .build();

                ShareDialog shareDialog = new ShareDialog(MainActivity.this);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
            }

            @Override
            public void onCancel() {
                Log.d("Success", "TayPVS - Cancel Login");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Success", "TayPVS - Error Login: " + error.toString());
            }
        });

        // Get hash key for Facebook
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "app.camnanglaixe.com.camnanglaixe",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", "TayPVS - Hash Key : " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        init();
    }

    protected void init(){

        addTopics();
        topicGridView = (ListView) findViewById(R.id.mainGridLayout);
        listTopicAdapter = new ListTopicAdapter(this, topics);
        listTopicAdapter.notifyDataSetChanged();
        topicGridView.setAdapter(listTopicAdapter);
        topicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isTopicXuphat = false;
                if (i == 0) {
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                } else {
                    if(topics.get(i).name.toLowerCase().contains("xử phạt"))
                        isTopicXuphat = true;
                    startSubActivity(topics.get(i).type_name, isTopicXuphat, i - 1);
                }
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
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
                Log.d("Log", "Log : - Ad Open");
                mAdView.setVisibility(View.GONE);
            }
        });

    }

    private void addTopics(){
        topics = new ArrayList<Topic>();
        try {
            for (int i = 0; i < Constanst.NUM_OF_TOPICS; i++) {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.getString(getBaseContext(), PreferenceUtils.TOPIC_NUMBER + i));
                Topic topic = JsonParseMachine.parseTopic(jsonObject);
                Log.d("TayPVS", "TayPVS - topics : " + topic.name);
                topics.add(topic);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        // Add Facebook
        Topic FBTopic = new Topic();
        FBTopic.name = "Chia sẻ app trên Facebook";
        FBTopic.type_name = "facebook";
        topics.add(0, FBTopic);

    }

    // Check screen orientation or screen rotate event here
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
}
