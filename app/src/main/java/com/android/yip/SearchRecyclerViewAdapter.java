package com.android.yip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {
    private final List<Business> mValues;
    private final SearchListFragment.OnSearchListFragmentInteractionListener mListener;
    private final Context mContext;

    public SearchRecyclerViewAdapter(List<Business> items,
                                     SearchListFragment.OnSearchListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
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
        // for faster image loading, check to see if we already loaded the image before
        if (holder.mItem.mImage != null) {
            holder.mPicture.setImageBitmap(holder.mItem.mImage);
            holder.mProgressBar.setVisibility(View.GONE);
        } else {
            loadImage(holder.mPicture, holder.mItem.mImageUrl, holder.mProgressBar);
        }
        RatingLoader ratingLoader = new RatingLoader();
        ratingLoader.setRatingsDrawable(holder.mItem.mRating, holder.mRating);
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

    private void loadImage(ImageView imageView, String url, final ProgressBar progressBar) {
        Glide
                .with(mContext)
                .load(url)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
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
        public final ProgressBar mProgressBar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = (ImageView) view.findViewById(R.id.fm_business_image);
            mName = (TextView) view.findViewById(R.id.fm_business_name);
            mRating = (ImageView) view.findViewById(R.id.fm_business_rating);
            mReviews = (TextView) view.findViewById(R.id.fm_business_review_count);
            mCategory = (TextView) view.findViewById(R.id.fm_business_category);
            mAddress = (TextView) view.findViewById(R.id.fm_business_address);
            mProgressBar = (ProgressBar) view.findViewById(R.id.fm_image_progress);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
