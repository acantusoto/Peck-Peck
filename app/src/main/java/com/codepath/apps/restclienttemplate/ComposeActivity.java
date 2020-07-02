package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;

    TwitterClient client;

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String  TAG = "ComposeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComposeBinding binding = ActivityComposeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);

        etCompose = binding.etCompose;
        btnTweet = binding.btnTweet;

        //Set Click Listener
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                Log.d(TAG, "onClick: "+tweetContent);
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this,"Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                    //make api call to post tweet
                    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess: to publish tweet");
                            try {

                                Tweet tweet =Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "Published tweet says: "+tweet);
                                Intent intent = new Intent();
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                //set Result code
                                setResult(RESULT_OK,intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure: to publish tweet",throwable );
                        }
                    });
                }

            }
        });
        // Make API call to Twitter to publish the tweet
    }
}