package com.example.android.bigscreen.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.bigscreen.data.MovieInfo;
import com.example.android.bigscreen.data.Repository;
import com.example.android.bigscreen.data.RepositoryCallbacks;
import com.example.android.bigscreen.data.Review;
import com.example.android.bigscreen.data.Trailer;

import java.util.ArrayList;

public class DetailActivityViewModel extends AndroidViewModel {
    private MutableLiveData<MovieInfo> mMovieInfoLiveData;
    private MutableLiveData<ArrayList<Trailer>> mTrailerInfosLiveData;
    private MutableLiveData<ArrayList<Review>> mReviewInfosLiveData;
    private int mMovieId;
    final private Repository mRepository;

    public DetailActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getRepository(application);
    }

    public MutableLiveData<MovieInfo> getMovieInfo(){
        if(mMovieInfoLiveData == null){
            mMovieInfoLiveData = new MutableLiveData<>();
        }
        return mMovieInfoLiveData;
    }

    public void setMovieInfo(MovieInfo movieInfo){
        mMovieId = movieInfo.getId();
        getMovieInfo().postValue(movieInfo);
    }

    public MutableLiveData<ArrayList<Trailer>> getTrailerInfos(){
        if(mTrailerInfosLiveData == null){
            mTrailerInfosLiveData = new MutableLiveData<>();
        }
        return mTrailerInfosLiveData;
    }

    public void setTrailerInfos(){
        mRepository.getTrailerList(mMovieId, new RepositoryCallbacks<ArrayList<Trailer>>() {
            @Override
            public void onSuccess(ArrayList<Trailer> trailers) {
                getTrailerInfos().postValue(trailers);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public MutableLiveData<ArrayList<Review>> getReviewInfos(){
        if(mReviewInfosLiveData == null){
            mReviewInfosLiveData = new MutableLiveData<>();
        }
        return mReviewInfosLiveData;
    }

    public void setReviewInfos(){
        mRepository.getReviewList(mMovieId, new RepositoryCallbacks<ArrayList<Review>>() {
            @Override
            public void onSuccess(ArrayList<Review> reviews) {
                getReviewInfos().postValue(reviews);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void toggleFavoriteButton() {
        if (mMovieInfoLiveData.getValue().isFavorite()) {
            MovieInfo movieInfo = mMovieInfoLiveData.getValue();
            mRepository.deleteFavoritedMovie(movieInfo);
            movieInfo.setFavorite(false);
            mMovieInfoLiveData.postValue(movieInfo);
        }
        else {
            MovieInfo movieInfo = mMovieInfoLiveData.getValue();
            movieInfo.setFavorite(true);
            mRepository.insertFavoriteMovie(movieInfo);
            mMovieInfoLiveData.postValue(movieInfo);
        }
    }

}
