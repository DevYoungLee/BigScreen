package com.example.android.bigscreen.userInterface;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bigscreen.R;
import com.example.android.bigscreen.data.Review;

import java.util.ArrayList;

public class DetailActivityReviewRecyclerViewAdapter extends RecyclerView.Adapter<DetailActivityReviewRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Review> mReviews;

    public DetailActivityReviewRecyclerViewAdapter(Context context, ArrayList<Review> reviews){
        mContext = context;
        mReviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mReviewAuthorTextView.setText(review.getAuthor());
        holder.mReviewContentTextView.setText(review.getContent());


    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mReviewAuthorTextView;
        final TextView mReviewContentTextView;
        final CardView mReviewItemCardView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mReviewItemCardView = itemView.findViewById(R.id.cv_review_item);
            mReviewAuthorTextView = itemView.findViewById(R.id.tv_review_author);
            mReviewContentTextView = itemView.findViewById(R.id.tv_review_content);

        }
    }
}
