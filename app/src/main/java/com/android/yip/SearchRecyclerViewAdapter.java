package com.android.yip;

import android.graphics.Bitmap;
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
        holder.mName.setText("Business Name: really long text that should be able to scroll" + position);
        holder.mAddress.setText("Address: " + position);
        holder.mCategory.setText("Category: " +position);
//        holder.mAddress.setText(holder.mItem.mAddress);
        // TODO: create a loadProfileImage method to create a bitmap from the item's image url
//        holder.mContentView.setText(mValues.get(position).content);
        // TODO: create a loadRatings method to set the stars image of the item based on its rating
//        String rating = mValues.get(position).rating;
//        loadRatings(rating);

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
//    public Bitmap loadProfileImage(String url) {
//        Bitmap bm = new Bitmap();
//        Use Glide library to load image from URL
//        return bm;
//    }

    // TODO: load the correct stars image based on a rating
    public void loadRatings(String rating) {
        // bunch of case statements
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