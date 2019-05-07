package com.android.yip;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yip.dummy.DummyContent.DummyItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link com.android.yip.SearchListFragment.OnSearchListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private final List<Business> mValues;
    private final SearchListFragment.OnSearchListFragmentInteractionListener mListener;

    public SearchRecyclerViewAdapter(List<Business> items,
                                     SearchListFragment.OnSearchListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(holder.mItem.mName);
//        String address =
//                holder.mItem.mAddress1 + ", " + holder.mItem.mCity + ", " + holder.mItem.mState + " " + holder.mItem.mZipcode + ", " + holder.mItem.mCountry;
        holder.mReviews.setText(String.valueOf(holder.mItem.mReviewCount));
        holder.mAddress.setText(holder.mItem.mAddress);
        holder.mCategory.setText(holder.mItem.mCategories);

        // TODO: create a loadProfileImage method to create a bitmap from the item's image url
//        Bitmap image = loadImage(holder.mItem.mImageUrl);
//        holder.mPicture.setImageBitmap(image);

        // Set the appropriate stars drawable based on a business' rating
        String rating = holder.mItem.mRating;
        switch (rating) {
            case "0.0":
                holder.mRating.setImageResource(R.drawable.stars_small_0);
                break;
            case "1.0":
                holder.mRating.setImageResource(R.drawable.stars_small_1);
                break;
            case "1.5":
                holder.mRating.setImageResource(R.drawable.stars_small_1_half);
                break;
            case "2.0":
                holder.mRating.setImageResource(R.drawable.stars_small_2);
                break;
            case "2.5":
                holder.mRating.setImageResource(R.drawable.stars_small_2_half);
                break;
            case "3.0":
                holder.mRating.setImageResource(R.drawable.stars_small_3);
                break;
            case "3.5":
                holder.mRating.setImageResource(R.drawable.stars_small_3_half);
                break;
            case "4.0":
                holder.mRating.setImageResource(R.drawable.stars_small_4);
                break;
            case "4.5":
                holder.mRating.setImageResource(R.drawable.stars_small_4_half);
                break;
            case "5.0":
                holder.mRating.setImageResource(R.drawable.stars_small_5);
                break;
            default:
                break;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSearchListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    // TODO: create bitmap so it can be used to set the profile image
//    public Bitmap loadImage(String url) {
//        Bitmap bm = new Bitmap();
//        Use Glide library to load image from URL
//        return bm;
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPicture;
        public final TextView mName;
        public final ImageView mRating;
        public final TextView mReviews;
        public final TextView mCategory;
        public final TextView mAddress;
        public Business mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = (ImageView) view.findViewById(R.id.fm_business_image);
            mName = (TextView) view.findViewById(R.id.fm_business_name);
            mRating = (ImageView) view.findViewById(R.id.fm_business_rating);
            mReviews = (TextView) view.findViewById(R.id.fm_business_review_count);
            mCategory = (TextView) view.findViewById(R.id.fm_business_category);
            mAddress = (TextView) view.findViewById(R.id.fm_business_address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
