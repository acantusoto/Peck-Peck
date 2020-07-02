package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.FragmentComposeTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeTweetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeTweetFragment extends DialogFragment  {

    EditText etCompose;
    Button btnTweet;
    FragmentComposeTweetBinding binding;
    View view;

    TwitterClient client;

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String  TAG = "ComposeActivity";

    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ComposeTweet.
     */
    public static ComposeTweetFragment newInstance() {
        ComposeTweetFragment fragment = new ComposeTweetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApp.getRestClient(getActivity());
        binding = FragmentComposeTweetBinding.inflate(getLayoutInflater());
        etCompose = binding.etCompose;
        btnTweet = binding.btnTweet;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        client = TwitterApp.getRestClient(getActivity());
        view = binding.getRoot();
        etCompose = binding.etCompose;
        btnTweet = binding.btnTweet;
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                Log.d(TAG, "onClick: "+tweetContent);
                if(tweetContent.isEmpty()){
                    Toast.makeText(getContext(),"Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(getContext(), "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), tweetContent, Toast.LENGTH_LONG).show();
                    //make api call to post tweet
                    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "onSuccess: to publish tweet");
                            try {

                                Tweet tweet =Tweet.fromJson(json.jsonObject);
                                ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
                                listener.onFinishComposeTweetDialog(tweet);
                                Toast.makeText(getContext(), "Tweet tweeted", Toast.LENGTH_SHORT).show();
                                dismiss();
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
        return view;
    }
    // 1. Defines the listener interface with a method passing back data result.
    public interface ComposeTweetDialogListener {
        void onFinishComposeTweetDialog(Tweet tweet);
    }
}