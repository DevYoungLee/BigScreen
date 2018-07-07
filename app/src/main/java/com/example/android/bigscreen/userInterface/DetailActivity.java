package com.example.android.bigscreen.userInterface;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.bigscreen.R;
import com.example.android.bigscreen.data.MovieInfo;
import com.example.android.bigscreen.data.Review;
import com.example.android.bigscreen.data.Trailer;
import com.example.android.bigscreen.network.NetworkApi;
import com.example.android.bigscreen.viewModel.DetailActivityViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private DetailActivityViewModel mDetailActivityViewModel;
    private int mRecyclerViewPosition;


    @BindView(R.id.tv_detail_title) TextView mDetailTitleTextView;
    @BindView(R.id.tv_detail_rating) TextView mDetailRatingTextView;
    @BindView(R.id.tv_detail_release_date) TextView mDetailReleaseDateTextView;
    @BindView(R.id.iv_detail_image_view) ImageView mDetailPosterImageView;
    @BindView(R.id.tv_detail_overview) TextView mDetailOverviewTextView;
    @BindView(R.id.ib_favorite) ImageButton mFavoriteImageButton;
    @BindView(R.id.trailer_button) Button mTrailerButton;
    @BindView(R.id.tv_reviews_header) TextView mReviewHeaderTextView;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        connectRecyclerView();
        connectFavoriteButton();


        Intent intent = getIntent();
        MovieInfo movieInfo = intent.getParcelableExtra(MainActivityRecyclerViewAdapter.INTENT_EXTRA_MOVIE_INFO);
        mRecyclerViewPosition = intent.getIntExtra(MainActivityRecyclerViewAdapter.INTENT_EXTRA_CLICKED_POSITION, 0);
        connectViewModel(movieInfo);


    }

    private void connectRecyclerView() {
        ArrayList<Review> placeHolder = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rv_review_list);
        LinearLayoutManager mLinearLayoutManager= new LinearLayoutManager(DetailActivity.this);
        mRecyclerViewAdapter = new DetailActivityReviewRecyclerViewAdapter(DetailActivity.this, placeHolder);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }


    private void connectFavoriteButton(){
        mFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailActivityViewModel.toggleFavoriteButton();
            }
        });
    }

    private void connectViewModel(MovieInfo movieInfo){
        mDetailActivityViewModel = ViewModelProviders.of(this).get(DetailActivityViewModel.class);
        connectMovieInfoLiveData(movieInfo);
        connectTrailerInfosLiveData();
        connectReviewInfosLiveData();
    }

    private void connectMovieInfoLiveData(MovieInfo movieInfo){
        mDetailActivityViewModel.setMovieInfo(movieInfo);
        if(movieInfo.isFavorite()) {
            mFavoriteImageButton.setImageResource(android.R.drawable.btn_star_big_on);
        }else {
            mFavoriteImageButton.setImageResource(android.R.drawable.btn_star_big_off);
        }
        mDetailActivityViewModel.getMovieInfo().observe(this, new Observer<MovieInfo>() {
            @Override
            public void onChanged(@Nullable MovieInfo movieInfo) {
                mDetailTitleTextView.setText(movieInfo.getTitle());
                String ratingText = String.valueOf(movieInfo.getVoteAverage()) + "/10";
                mDetailRatingTextView.setText(ratingText);
                mDetailReleaseDateTextView.setText(movieInfo.getReleaseDate());
                mDetailOverviewTextView.setText(movieInfo.getOverview());
                String urlString = NetworkApi.IMAGE_BASE_URL + movieInfo.getPosterPath();
                Picasso.get().load(urlString).error(R.drawable.ic_error).into(mDetailPosterImageView);
                if(movieInfo.isFavorite()) {
                    mFavoriteImageButton.setImageResource(android.R.drawable.btn_star_big_on);
                }else {
                    mFavoriteImageButton.setImageResource(android.R.drawable.btn_star_big_off);
                }
            }
        });
    }

    private void connectTrailerInfosLiveData(){

        mDetailActivityViewModel.setTrailerInfos();
        mDetailActivityViewModel.getTrailerInfos().observe(this, new Observer<ArrayList<Trailer>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Trailer> trailers) {
                if(!trailers.isEmpty()){
                    final String trailerKey = trailers.get(0).getKey();
                    mTrailerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailerKey));
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void connectReviewInfosLiveData(){
        mDetailActivityViewModel.setReviewInfos();
        mDetailActivityViewModel.getReviewInfos().observe(this, new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Review> reviews) {
                if(reviews.isEmpty()){
                    mReviewHeaderTextView.setVisibility(View.GONE);
                }
                else{
                    mReviewHeaderTextView.setVisibility(View.VISIBLE);
                }
                updateReviews(reviews);
            }
        });
    }


    @Override
    public void onBackPressed() {
        boolean isFavorite = mDetailActivityViewModel.getMovieInfo().getValue().isFavorite();
        Intent intent = new Intent();
        intent.putExtra(MainActivityRecyclerViewAdapter.INTENT_ON_RESULT_IS_FAVORITE, isFavorite);
        intent.putExtra(MainActivityRecyclerViewAdapter.INTENT_EXTRA_CLICKED_POSITION, mRecyclerViewPosition);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void updateReviews(ArrayList<Review> reviews){
        mRecyclerViewAdapter = new DetailActivityReviewRecyclerViewAdapter(DetailActivity.this, reviews);
        mRecyclerView.swapAdapter(mRecyclerViewAdapter, true);
    }

}
