package com.android.yip;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = "BusinessDetailActivity";
    private static final String EXTRA_ID = "com.android.yip.id";

    private String mBusinessId;
    private ImageView mPicture;
    private TextView mName;
    private ImageView mRating;
    private TextView mReviewCount;
    private TextView mCategories;
    private TextView mPrice;
    private TextView mIsClosed;
    private TextView mAddress;
    private TextView mDisplayPhone;
    private TextView mUrl;
    private RelativeLayout mHoursLayout;
    private LinearLayout mPhotosLayout;

    private TextView mReviewUserName;
    private TextView mReviewText;


    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        Bundle data = getIntent().getExtras();
        mBusinessId = data.getString(EXTRA_ID);
        Log.d(LOG_TAG, "ID: " +mBusinessId);

        mPicture = (ImageView) findViewById(R.id.business_detail_image);
        mName = (TextView) findViewById(R.id.business_detail_name);
        mRating = (ImageView) findViewById(R.id.business_detail_rating);
        mReviewCount = (TextView) findViewById(R.id.business_detail_review_count);
        mCategories = (TextView) findViewById(R.id.business_detail_category);
        mPrice = (TextView) findViewById(R.id.business_detail_price);
        mIsClosed = (TextView) findViewById(R.id.business_detail_isclosed);
        mAddress = (TextView) findViewById(R.id.business_detail_address);
        mDisplayPhone = (TextView) findViewById(R.id.business_detail_phone);
        mUrl = (TextView) findViewById(R.id.business_detail_url);
        mHoursLayout = (RelativeLayout) findViewById(R.id.business_detail_rl_hours);
        mPhotosLayout = (LinearLayout) findViewById(R.id.business_detail_linearlayout_photos);

        // TODO: remove this once finished with programmatically adding reviews
        mReviewUserName = (TextView) findViewById(R.id.business_detail_review_name);
        mReviewText = (TextView) findViewById(R.id.business_detail_review_text);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();

        String endpoint = getString(R.string.yelp_endpoint_business) + mBusinessId;
        String reviewEndpoint = endpoint + getString(R.string.yelp_endpoint_business_reviews);
        String locale = "";

        HashMap<String, String> parameters = new HashMap<>();
        // Build the URI using various parameters
        if (!locale.isEmpty()) {
            parameters.put("locale", locale);
        }
        runBusinessQuery(endpoint, parameters);
        runReviewQuery(reviewEndpoint, parameters);

    }


    /**
     * @param path the endpoints for the request
     * @param hashMap the map that has the key, value pairs of parameters
     * @return the complete URL string
     */
    private String getURL(String path, HashMap<String, String> hashMap) {
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.request_protocol))
                .authority(getString(R.string.yelp_api_domain))
                .path(path);

        // get every value f
        for (HashMap.Entry<String, String> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException exception) {
                Log.d(LOG_TAG, exception.getMessage());
            }
            builder.appendQueryParameter(key, value);
        }
        builder.build();
        url = builder.toString();
        return url;
    }


    private void runBusinessQuery(String endpoint, HashMap<String,String> parameters) {
        String tag_json_obj_search = "json_obj_business_req";

        String url = getURL(endpoint, parameters);
        Log.d(LOG_TAG, "URL: " + url);

        mProgressDialog.setMessage(getString(R.string.loading_request));
        mProgressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Business business = new Business(response, getApplicationContext());
                        displayBusinessDetails(business);
                        mProgressDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = getString(R.string.volley_error) + error.getMessage();
                mName.setText(message);
                mProgressDialog.hide();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // TODO: Authentication
                headers.put(getString(R.string.request_header_key),
                        getString(R.string.request_header_value) + " " + getString(R.string.yelp_api_key));
                return headers;
            }
        };
        VolleyQueueSingleton
                .getInstance(getApplicationContext())
                .addToRequestQueue(jsonObjReq, tag_json_obj_search);
    }

    private void runReviewQuery(String endpoint, HashMap<String,String> parameters) {
        String tag_json_obj_search = "json_obj_review_req";

        String url = getURL(endpoint, parameters);
        Log.d(LOG_TAG, "URL: " + url);

        mProgressDialog.setMessage(getString(R.string.loading_request));
        mProgressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ContentList list = ContentList.get(getApplicationContext());
                        List<Review> reviewList = list.getReviews();
                        reviewList.clear();
                        try {
                            JSONArray reviewsArray = (JSONArray) response.get("reviews");
                            for (int i = 0; i < reviewsArray.length(); i++) {
                                JSONObject reviewJO = (JSONObject) reviewsArray.get(i);
                                Review review = new Review(reviewJO, getApplicationContext());
                                reviewList.add(review);
                            }
                            displayReviews(reviewList);
                            mProgressDialog.hide();
                        } catch (JSONException exception) {
                            Log.d(LOG_TAG, exception.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = getString(R.string.volley_error) + error.getMessage();
                mName.setText(message);
                mProgressDialog.hide();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // TODO: Authentication
                headers.put(getString(R.string.request_header_key),
                        getString(R.string.request_header_value) + " " + getString(R.string.yelp_api_key));
                return headers;
            }
        };
        VolleyQueueSingleton
                .getInstance(getApplicationContext())
                .addToRequestQueue(jsonObjReq, tag_json_obj_search);
    }


    private void displayBusinessDetails(Business business) {
        loadBusinessImage(business.mImageUrl);
        mName.setText(business.mName);
        setRatingsDrawable(business.mRating, mRating);

        mReviewCount.setText(String.valueOf(business.mReviewCount));
        mCategories.setText(business.mCategories);
        mPrice.setText(business.mPrice);
        if (business.mIsClosed) {
            mIsClosed.setText(R.string.business_detail_isclosed);
            mIsClosed.setTextColor(getResources().getColor(R.color.colorRed));
        } else {
            mIsClosed.setText(R.string.business_detail_isopen);
            mIsClosed.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        mAddress.setText(business.mAddress);
        mDisplayPhone.setText(business.mDisplayPhone);
        mUrl.setText(business.mUrl);
        loadBusinessGallery(business.mPhotos);
    }

    private void setRatingsDrawable(String rating, ImageView mRating) {
        switch (rating) {
            case "0.0":
                mRating.setImageResource(R.drawable.stars_small_0);
                break;
            case "1.0":
                mRating.setImageResource(R.drawable.stars_small_1);
                break;
            case "1.5":
                mRating.setImageResource(R.drawable.stars_small_1_half);
                break;
            case "2.0":
                mRating.setImageResource(R.drawable.stars_small_2);
                break;
            case "2.5":
                mRating.setImageResource(R.drawable.stars_small_2_half);
                break;
            case "3.0":
                mRating.setImageResource(R.drawable.stars_small_3);
                break;
            case "3.5":
                mRating.setImageResource(R.drawable.stars_small_3_half);
                break;
            case "4.0":
                mRating.setImageResource(R.drawable.stars_small_4);
                break;
            case "4.5":
                mRating.setImageResource(R.drawable.stars_small_4_half);
                break;
            case "5.0":
                mRating.setImageResource(R.drawable.stars_small_5);
                break;
            default:
                break;
        }
    }

    private void loadBusinessImage(String url) {
        Glide
            .with(this)
            .load(url)
            .into(mPicture);
    }

    private void loadBusinessGallery(ArrayList<String> photos) {
        int cardMargins =
                getResources().getDimensionPixelSize(R.dimen.business_detail_card_margins);
        int elevation =
                getResources().getDimensionPixelSize(R.dimen.business_detail_card_elevation);
        int radius =
                getResources().getDimensionPixelSize(R.dimen.business_detail_card_radius);
        int cardPadding =
                getResources().getDimensionPixelSize(R.dimen.business_detail_card_padding);
        int photoWidth =
                getResources().getDimensionPixelSize(R.dimen.business_detail_photo_width);
        int photoHeight =
                getResources().getDimensionPixelSize(R.dimen.business_detail_photo_height);
        int photoPadding =
                getResources().getDimensionPixelSize(R.dimen.business_detail_photo_padding);

        RelativeLayout.LayoutParams cardParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(cardMargins, cardMargins, cardMargins, cardMargins);

        RelativeLayout.LayoutParams imageParams =
                new RelativeLayout.LayoutParams(photoWidth, photoHeight);

        for (String url : photos) {
            // create the cardviews in which the imageviews will be displayed in
            CardView cardView = new CardView(this);
            cardView.setLayoutParams(cardParams);
            cardView.setElevation(elevation);
            cardView.setRadius(radius);
            cardView.setUseCompatPadding(true);
            cardView.setContentPadding(cardPadding, cardPadding, cardPadding, cardPadding);

            // create the imageviews that display each photos
            ImageView photoView = new ImageView(this);
            photoView.setLayoutParams(imageParams);
            photoView.setPadding(photoPadding, photoPadding, photoPadding, photoPadding);
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // load the photos into each imageview
            Glide
                .with(this)
                .load(url)
                .into(photoView);

            // put the imageview inside a cardview
            cardView.addView(photoView);
            // put the cardview inside the layout
            mPhotosLayout.addView(cardView);
        }
    }

    private void displayReviews(List<Review> reviewsList) {
        for (Review review : reviewsList) {
            HashMap<String, String> user = review.mUser;
            mReviewUserName.setText(user.get("name"));
            mReviewText.setText(review.mText);
        }
    }


}
