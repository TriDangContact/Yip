package com.android.yip;

import org.json.JSONArray;
import org.json.JSONObject;

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


    public String toString() {
        return mName + " " + mId + ", ";
    }

}
