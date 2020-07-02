package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

//Annotation for parcler
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
@Parcel
public class Tweet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    public Long id;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public String body;

    @ColumnInfo
    public long userId;

    @Ignore
    public User user;

    @Ignore
    public String imageUrl;
//    public String displayUrl2;
//    public String displayUrl3;
//    public String displayUrl4;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = user;
        tweet.id = jsonObject.getLong("id");
        tweet.userId = user.id;
        try{
            tweet.imageUrl = jsonObject.getJSONObject("entities").getJSONArray("media").
                    getJSONObject(0).getString("media_url_https");

        }
        catch (JSONException e){

        }
        return tweet;
    }
    //empty constructor for parcel
    public Tweet(){}

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i =0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
