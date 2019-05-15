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
    private Context mContext;

    public Review() { initialize(); }

    public Review(JSONObject reviewJO, Context context) {
        initialize();

        mContext = context;
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
            mId =
                    reviewJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_id));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewRating(JSONObject reviewJO) {
        try {
            mRating =
                    (String) String.valueOf(reviewJO.getDouble(mContext.getResources().getString(R.string.yelp_response_business_reviews_rating)));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewUser(JSONObject reviewJO) {
        try {
            JSONObject userJO =
                    (JSONObject) reviewJO.get(mContext.getResources().getString(R.string.yelp_response_business_reviews_user));
            mUser.put(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_id),
                    userJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_id)));
            mUser.put(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_profile_url),
                    userJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_profile_url)));
            mUser.put(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_image_url),
                    userJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_image_url)));
            mUser.put(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_name),
                    userJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_user_name)));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewText(JSONObject reviewJO) {
        try {
            mText =
                    reviewJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_text));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewTimeCreated(JSONObject reviewJO) {
        try {
            mTimeCreated =
                    reviewJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_time_created));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setReviewUrl(JSONObject reviewJO) {
        try {
            mUrl =
                    reviewJO.getString(mContext.getResources().getString(R.string.yelp_response_business_reviews_url));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

}
