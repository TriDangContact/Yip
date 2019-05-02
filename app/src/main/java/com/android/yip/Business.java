package com.android.yip;

import org.json.JSONArray;
import org.json.JSONObject;

public class Business {
    public String mId;
    public String mAlias;
    public String mName;
    public String mImageUrl;
    public Boolean mIsClosed;
    public String mUrl;
    public String mPhone;
    public int mReviewCount;
    public String mCategories;
    public String mRating;
    public String mAddress;
    public String mLatitude;
    public String mLongitude;
    public String[] mPhotos;
    public String mPrice;
    public String mHours;


    public Business() {}


    // TODO: takes in a jsonOBject and instantiates the Business object using its values
    public void createBusiness(JSONObject jsonObject) {

    }

}
