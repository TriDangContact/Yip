package com.android.yip;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Review {
    private static final String LOG_TAG = "ReviewClass";
    public String mId;
    public String mRating;
    public HashMap<String, String> mUser;
    public String mText;
    public String mTimeCreated;
    public String mUrl;

    public Review() { initialize(); }

    public Review(JSONObject reviewJO, Context context) {
        initialize();

        setReviewId(reviewJO);
        setReviewRating(reviewJO);
        setReviewUser(reviewJO);
        setReviewText(reviewJO);
        setReviewTimeCreated(reviewJO);
        setReviewUrl(reviewJO);
    }

    private void initialize() {
        mId = "";
        mRating = "";
        mUser = new HashMap<>();
        mText = "";
        mTimeCreated = "";
        mUrl = "";
    }

    public void setReviewId(JSONObject reviewJO) {
        try {
            mId = reviewJO.getString("id");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewRating(JSONObject reviewJO) {
        try {
            mRating = reviewJO.getString("rating");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewUser(JSONObject reviewJO) {
        try {
            JSONObject userJO = (JSONObject) reviewJO.get("user");
            mUser.put("id", userJO.getString("id"));
            mUser.put("profile_url", userJO.getString("profile_url"));
            mUser.put("image_url", userJO.getString("image_url"));
            mUser.put("name", userJO.getString("name"));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewText(JSONObject reviewJO) {
        try {
            mText = reviewJO.getString("text");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewTimeCreated(JSONObject reviewJO) {
        try {
            mTimeCreated = reviewJO.getString("time_created");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewUrl(JSONObject reviewJO) {
        try {
            mUrl = reviewJO.getString("url");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

}
