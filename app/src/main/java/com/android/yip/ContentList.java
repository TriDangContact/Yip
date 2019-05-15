package com.android.yip;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class ContentList {

    private static ContentList sContentList;
    private static List<Business> mBusinessArrayList;
    private static List<Review> mReviewArrayList;
    private static List<String> mSuggestionArrayList;

    public static ContentList get(Context context){
        if (sContentList == null) {
            sContentList = new ContentList(context);
        }
        return sContentList;
    }


    private ContentList(Context context){
        mBusinessArrayList = new ArrayList<Business>();
        mReviewArrayList = new ArrayList<Review>();
        mSuggestionArrayList = new ArrayList<String>();
    }

    public List<Business> getBusinesses(){ return mBusinessArrayList; }

    public List<Review> getReviews() { return mReviewArrayList; }

    public List<String> getSuggestions() { return mSuggestionArrayList; }

}

