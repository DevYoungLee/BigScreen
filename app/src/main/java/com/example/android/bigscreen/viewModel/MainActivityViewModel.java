package com.example.android.bigscreen.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.example.android.bigscreen.data.Repository;
import com.example.android.bigscreen.data.RepositoryCallbacks;
import com.example.android.bigscreen.data.MovieInfo;
import java.util.ArrayList;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<MovieInfo>> mMovieInfosLiveData;
    private boolean mIsLoadingData;
    final private Repository mRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getRepository(application);
    }

    public MutableLiveData<ArrayList<MovieInfo>> getMovieInfos(){
        if(mMovieInfosLiveData == null){
            mMovieInfosLiveData = new MutableLiveData<>();
        }
        return mMovieInfosLiveData;
    }

    public void setMovieList(String sortBy, int page) {
        mIsLoadingData = true;
        mRepository.getMovieList(sortBy, page, new RepositoryCallbacks<ArrayList<MovieInfo>>() {
        @Override
        public void onSuccess(ArrayList<MovieInfo> movieInfos) {
            checkForFavoritedMovies(movieInfos);
            mMovieInfosLiveData.postValue(movieInfos);
            mIsLoadingData = false;
        }

        @Override
        public void onFailure(Throwable throwable) {
            throwable.printStackTrace();
        }
    });
    }

    public void setFavoritedMovieList(){
        mIsLoadingData = true;
        mRepository.getAllFavoriteMovies(new RepositoryCallbacks<ArrayList<MovieInfo>>() {
            @Override
            public void onSuccess(ArrayList<MovieInfo> movieInfos) {
                mMovieInfosLiveData.postValue(movieInfos);
                mIsLoadingData = false;
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

    public boolean ismIsLoadingData(){
        return mIsLoadingData;
    }

    public void loadMoreData(String sortBy, int nextPage){
        mIsLoadingData = true;
        mRepository.getMovieList(sortBy, nextPage, new RepositoryCallbacks<ArrayList<MovieInfo>>() {
            @Override
            public void onSuccess(ArrayList<MovieInfo> movieInfos) {
                ArrayList<MovieInfo> movieInfoList = mMovieInfosLiveData.getValue();
                checkForFavoritedMovies(movieInfos);
                movieInfoList.addAll(movieInfos);
                mMovieInfosLiveData.postValue(movieInfoList);
                mIsLoadingData = false;


            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void updateSingleMovieInfo(int position, boolean isFavorite){
        ArrayList<MovieInfo> movieInfoList = mMovieInfosLiveData.getValue();
        movieInfoList.get(position).setFavorite(isFavorite);
        mMovieInfosLiveData.postValue(movieInfoList);
    }

    private void checkForFavoritedMovies(final  ArrayList<MovieInfo> movieInfosToBeChecked){
        mRepository.getAllFavoriteMovies(new RepositoryCallbacks<ArrayList<MovieInfo>>() {
            @Override
            public void onSuccess(ArrayList<MovieInfo> movieInfos) {
                if(movieInfos != null) {
                    for (int i = 0; i < movieInfosToBeChecked.size(); i++) {
                        int movieId = movieInfosToBeChecked.get(i).getId();
                        for (int j = 0; j < movieInfos.size(); j++) {
                            int favoriteMovieId = movieInfos.get(j).getId();
                            if (movieId == favoriteMovieId) {
                                movieInfosToBeChecked.get(i).setFavorite(true);
                                break;

                            }

                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

}
