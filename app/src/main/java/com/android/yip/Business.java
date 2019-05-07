package com.android.yip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;


public class Business {
    public String mId = "";
    public String mAlias = "";
    public String mName = "";
    public String mImageUrl = "";
    public Boolean mIsClosed;
    public String mUrl = "";
    public String mPhone = "";
    public int mReviewCount = 0;
    public String mCategories = "";
    public String mRating = "0.0";
    public String mAddress;
    public double mLatitude;
    public double mLongitude;
    public String[] mPhotos;
    public String mPrice = "";
    public String mHours = "";
    public Bitmap mImage;


    public Business() {}

    public void setBusinessAddress(String address1, String address2, String address3,
                                      String city, String state, String zipcode, String country) {
        String address = "";

        if (address1 != null && !address1.equals("null") && !address1.isEmpty()) {
            address = address + address1;
        }
        if (address2 != null && !address2.equals("null") && !address2.isEmpty()) {
            address = address + ", " + address2;
        }
        if (address3 != null && !address3.equals("null") &&  !address3.isEmpty()) {
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

    public String toString() {
        return mName + " " + mId + ", ";
    }

}
