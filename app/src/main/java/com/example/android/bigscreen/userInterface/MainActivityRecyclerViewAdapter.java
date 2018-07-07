package com.example.android.bigscreen.userInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.android.bigscreen.R;
import com.example.android.bigscreen.data.MovieInfo;
import com.example.android.bigscreen.network.NetworkApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.ViewHolder> {

    public static final String INTENT_EXTRA_MOVIE_INFO = "extra_movie_info";
    public static final String INTENT_EXTRA_CLICKED_POSITION = "extra_clicked_position";
    public static final String INTENT_ON_RESULT_IS_FAVORITE = "extra_is_favorite";
    public static final int RESULT_CODE_FOR_CHANGED_MOVIE = 1;


    private Context mContext;
    private ArrayList<MovieInfo> mMovieInfos;

    public MainActivityRecyclerViewAdapter(Context context, ArrayList<MovieInfo> movieInfos) {
        mContext = context;
        mMovieInfos = movieInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final MovieInfo movieInfo = mMovieInfos.get(position);
        final int ind = position;
        viewHolder.mRatingBar.setRating((float) (movieInfo.getVoteAverage()/2));
        String imagePath = movieInfo.getPosterPath();
        String urlString = NetworkApi.IMAGE_BASE_URL + imagePath;
        Picasso.get().load(urlString).error(R.drawable.ic_error).into(viewHolder.mPosterImageView);
        viewHolder.mMovieItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieInfo movieInfo = mMovieInfos.get(ind);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(INTENT_EXTRA_CLICKED_POSITION, ind);
                intent.putExtra(INTENT_EXTRA_MOVIE_INFO, movieInfo);
                ((Activity) mContext).startActivityForResult(intent, RESULT_CODE_FOR_CHANGED_MOVIE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMovieInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final CardView mMovieItemCardView;
        final ImageView mPosterImageView;
        final RatingBar mRatingBar;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mRatingBar = itemView.findViewById(R.id.rb_rating);
            mMovieItemCardView = itemView.findViewById(R.id.cv_movie_item);
            mPosterImageView = itemView.findViewById(R.id.iv_poster_image);
        }
    }
}
