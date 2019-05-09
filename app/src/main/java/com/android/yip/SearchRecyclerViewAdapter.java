package com.android.yip;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Business} and makes a call to the
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
        int listPosition = position + 1;
        holder.mItem = mValues.get(position);
        holder.mName.setText(listPosition + ". " + holder.mItem.mName);
        holder.mReviews.setText(String.valueOf(holder.mItem.mReviewCount));
        holder.mAddress.setText(holder.mItem.mAddress);
        holder.mCategory.setText(holder.mItem.mCategories);
        holder.mPicture.setImageBitmap(holder.mItem.mImage);
        setRatingsDrawable(holder.mItem.mRating, holder.mRating);
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
