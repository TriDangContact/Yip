package com.android.yip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Business {
    private static final String LOG_TAG = "BusinessClass";
    public String mId;
    public String mAlias;
    public String mName;
    public String mImageUrl;
    public Bitmap mImage;
    public Boolean mIsClosed;
    public String mUrl;
    public String mPhone;
    public String mDisplayPhone;
    public int mReviewCount;
    public String mCategories;
    public String mRating;
    public String mAddress;
    public double mLatitude;
    public double mLongitude;
    public ArrayList<String> mPhotos;
    public String mPrice;
    public ArrayList<JSONObject> mHours;

    public Business() {
        initialize();
    }

    // TODO: takes in a jsonOBject and instantiates the Business object using its values
    public Business(JSONObject businessJO, Context context) {
        initialize();

        setBusinessId(businessJO);
        setBusinessAlias(businessJO);
        setBusinessName(businessJO);
        setBusinessImageUrl(businessJO);
        if (mImageUrl != null && !mImageUrl.equals("null")) {
            setBusinessImage(mImageUrl, context);
        }
        setBusinessIsClosed(businessJO);
        setBusinessUrl(businessJO);
        setBusinessPhone(businessJO);
        setBusinessDisplayPhone(businessJO);
        setBusinessReviewCount(businessJO);
        setBusinessRating(businessJO);
        setBusinessPrice(businessJO);
        setBusinessAddress(businessJO);
        setBusinessCategories(businessJO);
        setBusinessCoordinates(businessJO);
        setBusinessPhotos(businessJO);
        setBusinessHours(businessJO);
    }

    private void initialize() {
        mId = "";
        mAlias = "";
        mName = "";
        mImageUrl = "";
        mImage = null;
        mIsClosed = true;
        mUrl = "";
        mPhone = "";
        mDisplayPhone = "";
        mReviewCount = 0;
        mCategories = "";
        mRating = "0.0";
        mPrice = "";
        mAddress = "";
        mLatitude = 0.0;
        mLongitude = 0.0;
        mPhotos = new ArrayList<>();
        mHours = new ArrayList<>();
    }

    public void setBusinessId(JSONObject businessJO) {
        try {
            mId = businessJO.getString("id");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessAlias(JSONObject businessJO) {
        try {
            mAlias = businessJO.getString("alias");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
            mAlias = "Error retrieving alias";
        }
    }

    public void setBusinessName(JSONObject businessJO) {
        try {
            mName = businessJO.getString("name");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
            mName = "Error retrieving name";
        }
    }
    public void setBusinessImageUrl(JSONObject businessJO) {
        try {
            mImageUrl = businessJO.getString("image_url");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessImage(String url, Context context) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mImage = resource;
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void setBusinessIsClosed(JSONObject businessJO) {
        try {
            mIsClosed = businessJO.getBoolean("is_closed");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessUrl(JSONObject businessJO) {
        try {
            mUrl = businessJO.getString("url");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessPhone(JSONObject businessJO) {
        try {
            mPhone = businessJO.getString("phone");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessDisplayPhone(JSONObject businessJO) {
        try {
            mDisplayPhone = businessJO.getString("display_phone");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessReviewCount(JSONObject businessJO) {
        try {
            mReviewCount = businessJO.getInt("review_count");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessRating(JSONObject businessJO) {
        try {
            mRating = (String) String.valueOf(businessJO.getDouble("rating"));
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessPrice(JSONObject businessJO) {
        try {
            mPrice = businessJO.getString("price");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
            mPrice = "Error retrieving price";
        }
    }

    public void setBusinessAddress(JSONObject businessJO) {
        String address1 = "";
        String address2 = "";
        String address3 = "";
        String city = "";
        String state = "";
        String zipcode = "";
        String country = "";
        String address = "";

        try {
            JSONObject locationObject = (JSONObject) businessJO.get("location");
            address1 = locationObject.getString("address1");
            address2 = locationObject.getString("address2");
            address3 = locationObject.getString("address3");
            city = locationObject.getString("city");
            state = locationObject.getString("state");
            zipcode = locationObject.getString("zip_code");
            country = locationObject.getString("country");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }

        if (address1 != null && !address1.equals("null") && !address1.isEmpty()) {
            address = address + address1;
        }
        if (address2 != null && !address2.equals("null") && !address2.isEmpty()) {
            address = address + ", " + address2;
        }
        if (address3 != null && !address3.equals("null") && !address3.isEmpty()) {
            address = address + ", " + address3;
        }
        if (city != null && !city.equals("null") && !city.isEmpty()) {
            address = address + ", " + city;
        }
        if (state != null && !state.equals("null") && !state.isEmpty()) {
            address = address + ", " + state;
        }
        if (zipcode != null && !zipcode.equals("null") && !zipcode.isEmpty()) {
            address = address + " " + zipcode;
        }
        if (country != null && !country.equals("null") && !country.isEmpty()) {
            address = address + ", " + country;
        }

        mAddress = address;
    }



    // Getting Categories from each business
    public void setBusinessCategories(JSONObject businessJO) {
        try {
            JSONArray categoriesArray = (JSONArray) businessJO.get("categories");
            if (categoriesArray.length() <= 0) {
                mCategories = "No categories";
            } else {
                if (categoriesArray.length() > 1) {
                    // get every category, up to the second to last one
                    for (int j = 0; j < categoriesArray.length() - 1; j++) {
                        JSONObject category = categoriesArray.getJSONObject(j);
                        String title = category.getString("title");
                        mCategories = mCategories.concat(title + ", ");
                    }
                }
                // add the last category if more than 1, or the only category if only 1
                JSONObject category = categoriesArray.getJSONObject(categoriesArray.length() - 1);
                String title = category.getString("title");
                mCategories = mCategories.concat(title);
            }
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
            mCategories = "Error retrieving categories";
        }
    }

    public void setBusinessCoordinates(JSONObject businessJO) {
        try {
            JSONObject coordinatesObject = (JSONObject) businessJO.get("coordinates");
            mLatitude = coordinatesObject.getDouble("latitude");
            mLongitude = coordinatesObject.getDouble("longitude");
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessPhotos(JSONObject businessJO) {
        try {
            JSONArray photosArray = (JSONArray) businessJO.getJSONArray("photos");
            if (photosArray != null) {
                for (int i = 0; i < photosArray.length(); i++) {
                    mPhotos.add(photosArray.get(i).toString());
                }
            }
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }

    public void setBusinessHours(JSONObject businessJO) {
        try {
            JSONArray hoursArray = (JSONArray) businessJO.getJSONArray("hours");
            if (hoursArray != null) {
                for (int i = 0; i < hoursArray.length(); i++) {
                    JSONObject hoursObject = (JSONObject) hoursArray.get(i);
                    JSONArray openArray = (JSONArray) hoursObject.get("open");
                    if (openArray != null) {
                        for (int j = 0; j < openArray.length(); j++) {
                            JSONObject dayObject = (JSONObject) openArray.get(j);
                            mHours.add(dayObject);
                        }
                    }
                }
            }
        } catch (JSONException exception) {
            Log.d(LOG_TAG, exception.toString());
        }
    }



    public String toString() {
        return mName + " " + mId + ", ";
    }

}
