package com.android.yip;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapsCustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View mMarkerView;
    private Context mContext;
    private ImageView mPicture;
    private TextView mName;
    private ImageView mRating;
    private TextView mReviews;
    private TextView mCategory;
    private TextView mAddress;

    MapsCustomInfoWindowAdapter(Context context) {
        mContext = context;
    }

    public View getInfoWindow(Marker marker) {
        return null;
    }

    public View getInfoContents(Marker marker) {
        mMarkerView =
                ((Activity) mContext).getLayoutInflater().inflate(R.layout.fragment_maps_info_window_adapter, null);
        render(marker, mMarkerView);
        return mMarkerView;
    }

    private void render(Marker marker, View view) {
        mPicture = (ImageView) view.findViewById(R.id.maps_info_window_business_image);
        mName = (TextView) view.findViewById(R.id.maps_info_window_business_name);
        mRating = (ImageView) view.findViewById(R.id.maps_info_window_business_rating);
        mReviews = (TextView) view.findViewById(R.id.maps_info_window_business_review_count);
        mCategory = (TextView) view.findViewById(R.id.maps_info_window_business_category);
        mAddress = (TextView) view.findViewById(R.id.maps_info_window_business_address);

        // Retrieve the business data from the marker
        Business business = (Business) marker.getTag();

        mName.setText(business.mName);
        mReviews.setText(String.valueOf(business.mReviewCount));
        mAddress.setText(business.mAddress);
        mCategory.setText(business.mCategories);
        mPicture.setImageBitmap(business.mImage);

        // Set the appropriate stars drawable based on a business' rating
        RatingLoader ratingLoader = new RatingLoader();
        ratingLoader.setRatingsDrawable(business.mRating, mRating);
    }

}
