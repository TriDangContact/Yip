package com.android.yip;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class ContentList {

    private static ContentList sContentList;
    private static List<Business> mBusinessArrayList;

    public static ContentList get(Context context){
        if (sContentList == null) {
            sContentList = new ContentList(context);
        }
        return sContentList;
    }


    private ContentList(Context context){
        mBusinessArrayList = new ArrayList<Business>();
    }

    public List<Business> getBusinesses(){ return mBusinessArrayList; }

//    public List<User> getUsers(){ return mUserArrayList; }
//
//    public List<String> getHashtags(){
//        return mHashtagArrayList;
//    }
}

