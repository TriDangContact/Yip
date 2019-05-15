package com.android.yip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private RelativeLayout mAddressLayout;
    private TextView mAddress;
    private RelativeLayout mDisplayPhoneLayout;
    private TextView mDisplayPhone;
    private RelativeLayout mUrlLayout;
    private TextView mUrl;
    private LinearLayout mHoursLayout;
    private LinearLayout mPhotosLayout;
    private LinearLayout mReviewLayout;
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
        mAddressLayout = (RelativeLayout) findViewById(R.id.business_detail_rl_address);
        mAddress = (TextView) findViewById(R.id.business_detail_address);
        mDisplayPhoneLayout = (RelativeLayout) findViewById(R.id.business_detail_rl_phone);
        mDisplayPhone = (TextView) findViewById(R.id.business_detail_phone);
        mUrlLayout = (RelativeLayout) findViewById(R.id.business_detail_rl_url);
        mUrl = (TextView) findViewById(R.id.business_detail_url);
        mHoursLayout = (LinearLayout) findViewById(R.id.business_detail_rl_hours);
        mPhotosLayout = (LinearLayout) findViewById(R.id.business_detail_linearlayout_photos);
        mReviewLayout = (LinearLayout) findViewById(R.id.business_detail_ll_review);

        // user must wait for business details to be retrieved from API
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();

        // configuring api endpoints
        String endpoint = getString(R.string.yelp_endpoint_business) + mBusinessId;
        String reviewEndpoint = endpoint + getString(R.string.yelp_endpoint_business_reviews);
        String locale = "";

        // Build the URI using various parameters
        HashMap<String, String> parameters = new HashMap<>();
        if (!locale.isEmpty()) {
            parameters.put(getString(R.string.yelp_parameters_business_search_locale), locale);
        }

        // retrieve the business data using passed in business id
        runBusinessQuery(endpoint, parameters);
        runReviewQuery(reviewEndpoint, parameters);

        mAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddress(mAddress.getText().toString());
            }
        });
        mDisplayPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer(mDisplayPhone.getText().toString());
            }
        });
        mUrlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(mUrl.getText().toString());
            }
        });
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
                Log.d(LOG_TAG, getString(R.string.volley_error) + error.getMessage());
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // we take every reviews for each business and add to our list so we can
                        // display them
                        ContentList list = ContentList.get(getApplicationContext());
                        List<Review> reviewList = list.getReviews();
                        reviewList.clear();
                        try {
                            JSONArray reviewsArray =
                                    (JSONArray) response.get(getString(R.string.yelp_response_business_reviews));
                            for (int i = 0; i < reviewsArray.length(); i++) {
                                JSONObject reviewJO = (JSONObject) reviewsArray.get(i);
                                Review review = new Review(reviewJO, getApplicationContext());
                                reviewList.add(review);
                            }
                            displayReviews(reviewList);
                        } catch (JSONException exception) {
                            Log.d(LOG_TAG, exception.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, getString(R.string.volley_error) + error.getMessage());
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
        mName.setText(business.mName);
        setTitle(business.mName);
        loadImage(business.mImageUrl, mPicture);

        RatingLoader ratingLoader = new RatingLoader();
        ratingLoader.setRatingsDrawable(business.mRating, mRating);
        mReviewCount.setText(String.valueOf(business.mReviewCount));
        mCategories.setText(business.mCategories);
        mPrice.setText(business.mPrice);
        if (business.mIsClosed) {
            mIsClosed.setText(R.string.business_detail_isclosed);
            mIsClosed.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        } else {
            mIsClosed.setText(R.string.business_detail_isopen);
            mIsClosed.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        }
        mAddress.setText(business.mAddress);
        mDisplayPhone.setText(business.mDisplayPhone);
        mUrl.setText(business.mUrl);
        displayHours(business.mHours);
        loadBusinessGallery(business.mPhotos);
    }

    private void setTitle(String title) {
        if (title.isEmpty()) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        } else {
            getSupportActionBar().setTitle(title);
        }
    }

    private void loadImage(String url, ImageView imageView) {
        Glide
            .with(this)
            .load(url)
            .centerCrop()
            .into(imageView);
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
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
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
            loadImage(url, photoView);

            // put the imageview inside a cardview
            cardView.addView(photoView);
            // put the cardview inside the layout
            mPhotosLayout.addView(cardView);
        }
    }

    private void displayReviews(List<Review> reviewsList) {
        int length = reviewsList.size();
        if (length > 0) {
            for (Review review : reviewsList) {
                HashMap<String, String> user = review.mUser;
                String userPhotoUrl = user.get(getString(R.string.yelp_response_business_reviews_user_image_url));
                String userName = user.get(getString(R.string.yelp_response_business_reviews_user_name));
                String reviewDate = review.mTimeCreated;
                String reviewRating = review.mRating;
                String reviewText = review.mText;
                final String reviewUrl = review.mUrl;

                /*
                 * create a new linear layout to place the review in
                 * android:layout_width="match_parent"
                 * android:layout_height="wrap_content"
                 * android:layout_marginBottom="@dimen/business_detail_ll_reviews_margin"
                 * android:orientation="vertical"
                 */
                int reviewRLMargin =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_ll_reviews_margin);
                LinearLayout.LayoutParams reviewRLParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                reviewRLParams.setMargins(0,0,0,reviewRLMargin);

                LinearLayout reviewRL = new LinearLayout(this);
                reviewRL.setLayoutParams(reviewRLParams);
                reviewRL.setOrientation(LinearLayout.VERTICAL);

                /// create a new linear layout for user photo and name
                int userReviewInfoMargin =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_ll_reviews_user_margin);

                LinearLayout.LayoutParams userLLParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                userLLParams.setMargins(0,userReviewInfoMargin,0,userReviewInfoMargin);

                LinearLayout userInfoLL = new LinearLayout(this);
                userInfoLL.setLayoutParams(userLLParams);
                userInfoLL.setOrientation(LinearLayout.HORIZONTAL);


                /// create an imageview to hold the user photo
                int userPhotoWidth =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_review_user_photo_width);
                int userPhotoHeight =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_review_user_photo_height);
                LinearLayout.LayoutParams userPhotoParams =
                        new LinearLayout.LayoutParams(
                                userPhotoWidth,
                                userPhotoHeight);
                ImageView userPhotoView = new ImageView(this);
                userPhotoView.setLayoutParams(userPhotoParams);

                loadImage(userPhotoUrl, userPhotoView);

                userInfoLL.addView(userPhotoView);

                /// create a textview to hold the user's name
                int userNameMargin =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_review_user_name_margin);
                LinearLayout.LayoutParams userNameParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                userNameParams.setMarginStart(userNameMargin);
                TextView userNameView = new TextView(this);
                userNameView.setLayoutParams(userNameParams);
                userNameView.setGravity(Gravity.CENTER_VERTICAL);
                userNameView.setTypeface(Typeface.DEFAULT_BOLD);
                userNameView.setText(userName);

                userInfoLL.addView(userNameView);

                /// create a new linear layout for review rating
                int userRatingMargin =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_review_user_rating_margin);
                LinearLayout ratingInfoLL = new LinearLayout(this);
                RelativeLayout.LayoutParams userRatingParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                userRatingParams.setMargins(0,userRatingMargin,0,userRatingMargin);
                ratingInfoLL.setLayoutParams(userRatingParams);
                ratingInfoLL.setOrientation(LinearLayout.HORIZONTAL);

                /// create an imageview to display rating
                int reviewRatingWidth =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_review_review_rating_width);
                RelativeLayout.LayoutParams reviewRatingParams =
                        new RelativeLayout.LayoutParams(reviewRatingWidth,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                ImageView reviewRatingView = new ImageView(this);
                reviewRatingView.setLayoutParams(reviewRatingParams);
                reviewRatingView.setContentDescription(getString(R.string.fm_search_item_rating_description));
                RatingLoader ratingLoader = new RatingLoader();
                ratingLoader.setRatingsDrawable(reviewRating, reviewRatingView);
                ratingInfoLL.addView(reviewRatingView);

                /// create a Textview to display review date
                int reviewDateMargin =
                        getResources().getDimensionPixelSize(R.dimen.business_detail_review_date_margin);
                RelativeLayout.LayoutParams reviewDateParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                reviewDateParams.setMarginStart(reviewDateMargin);
                TextView reviewDateView = new TextView(this);
                reviewDateView.setLayoutParams(reviewDateParams);
                reviewDateView.setGravity(Gravity.CENTER_VERTICAL);
                reviewDateView.setText(reviewDate);

                ratingInfoLL.addView(reviewDateView);

                /// create a textview to display review text
                RelativeLayout.LayoutParams reviewTextParams =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView ratingTextView = new TextView(this);
                ratingTextView.setLayoutParams(reviewTextParams);
                ratingTextView.setText(reviewText);

                /// add linear layout 1, linear layout 2, and textview to reviewRL
                reviewRL.addView(userInfoLL);
                reviewRL.addView(ratingInfoLL);
                reviewRL.addView(ratingTextView);

                TypedValue outValue = new TypedValue();
                getApplication().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue,
                        true);
                reviewRL.setBackgroundResource(outValue.resourceId);
                reviewRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWebPage(reviewUrl);
                    }
                });

                mReviewLayout.addView(reviewRL);

            }
        } else {
            // no reviews available
            LinearLayout.LayoutParams noReviewsTextParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView noReviewsText = new TextView(this);
            noReviewsText.setLayoutParams(noReviewsTextParams);
            noReviewsText.setText(R.string.business_detail_no_reviews);

            mReviewLayout.addView(noReviewsText);
        }
    }

    private void displayHours(ArrayList<JSONObject> hoursList) {
        for (JSONObject dayObject : hoursList) {
            String startTime = "";
            String endTime = "";
            int day = -1;
            try {
                startTime = dayObject.getString(getString(R.string.yelp_response_business_detail_hours_start));
                endTime = dayObject.getString(getString(R.string.yelp_response_business_detail_hours_end));
                day = dayObject.getInt(getString(R.string.yelp_response_business_detail_hours_day));
            } catch (JSONException exception) {
                Log.d(LOG_TAG, exception.toString());
            }

            // create relative layout to hold each dayObject
            RelativeLayout.LayoutParams hoursParams =
                    new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout hoursRL = new RelativeLayout(this);
            hoursRL.setLayoutParams(hoursParams);

            // create textview to display which day
            RelativeLayout.LayoutParams dayViewParams =
                    new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            dayViewParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            TextView dayView = new TextView(this);
            dayView.setLayoutParams(dayViewParams);
            String dayString = getDay(day);
            dayView.setText(dayString);

            hoursRL.addView(dayView);

            // create linear layout to hold the start/end time
            RelativeLayout.LayoutParams hoursLLParams =
                    new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            hoursLLParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            LinearLayout hoursLL = new LinearLayout(this);
            hoursLL.setLayoutParams(hoursLLParams);

            TextView startView = createStandardTextView(hoursLLParams);
            startTime = convert24hTo12h(startTime);
            startView.setText(startTime);

            TextView toView = createStandardTextView(hoursLLParams);
            toView.setText(R.string.business_detail_hours_time_to);

            TextView endView = createStandardTextView(hoursLLParams);
            endTime = convert24hTo12h(endTime);
            endView.setText(endTime);

            // add start/to/end view to the hoursLL
            hoursLL.addView(startView);
            hoursLL.addView(toView);
            hoursLL.addView(endView);

            hoursRL.addView(hoursLL);

            // add each hour object to its parent layout
            mHoursLayout.addView(hoursRL);
        }
    }

    private TextView createStandardTextView(RelativeLayout.LayoutParams params) {
        TextView newTextView = new TextView(this);
        newTextView.setLayoutParams(params);
        return newTextView;
    }

    private String getDay(int dayInt) {
        String day;
        switch (dayInt) {
            case 0:
                day = getString(R.string.business_detail_hours_monday);
                break;
            case 1:
                day = getString(R.string.business_detail_hours_tuesday);
                break;
            case 2:
                day = getString(R.string.business_detail_hours_wednesday);
                break;
            case 3:
                day = getString(R.string.business_detail_hours_thursday);
                break;
            case 4:
                day = getString(R.string.business_detail_hours_friday);
                break;
            case 5:
                day = getString(R.string.business_detail_hours_saturday);
                break;
            case 6:
                day = getString(R.string.business_detail_hours_sunday);
                break;
            default:
                day = getString(R.string.business_detail_hours_unknown);
                break;
        }
        return day;
    }

    private String convert24hTo12h(String time) {
        String time12h = "";
        if (time.length() == 4) {
            StringBuilder str = new StringBuilder(time);
            str.insert(2, ':');
            time12h = str.toString();
        }
        try {
            SimpleDateFormat hour24SDF = new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat hour12SDF = new SimpleDateFormat("h:mm a", Locale.US);
            Date dateObj = hour24SDF.parse(time12h);
            time12h = hour12SDF.format(dateObj);
        } catch (Exception e) {
            Log.d(LOG_TAG, e.toString());
        }
        return time12h.toLowerCase();
    }

    private void openAddress(String address) {
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this, getString(R.string.business_detail_open_map_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialer(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this, getString(R.string.business_detail_open_dialer_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this, getString(R.string.business_detail_open_browser_error), Toast.LENGTH_SHORT).show();
        }
    }

}
