package com.android.yip;

import android.widget.ImageView;

public class RatingLoader {

    public void setRatingsDrawable(String rating, ImageView mRating) {
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
}
