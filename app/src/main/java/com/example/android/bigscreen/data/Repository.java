package com.example.android.bigscreen.data;

import android.app.Application;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bigscreen.database.AppDatabase;
import com.example.android.bigscreen.database.MovieDao;
import com.example.android.bigscreen.network.NetworkApi;
import com.example.android.bigscreen.network.NetworkClient;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository INSTANCE;
    final private NetworkApi networkApi;
    final private MovieDao movieDao;

    private Repository(Application application){
        networkApi = NetworkClient.getNetworkApi();
        AppDatabase db = AppDatabase.getDatabase(application);
        movieDao = db.movieDao();
    }

    public static Repository getRepository(Application application){
            if(INSTANCE == null){
                synchronized (Repository.class){
                    if(INSTANCE == null){
                        INSTANCE = new Repository(application);
                    }
                }
            }
            return INSTANCE;
    }

    public void getMovieList (String sortBy, int page, final RepositoryCallbacks<ArrayList<MovieInfo>> callbacks){
        Call<MovieList> call = networkApi.getMovieList(
                sortBy,
                NetworkApi.API_KEY,
                NetworkApi.LANGUAGE,
                page);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                MovieList movieList = response.body();
                ArrayList<MovieInfo> movieInfoArrayList = null;
                if (movieList != null) {
                    movieInfoArrayList = movieList.getResults();
                    callbacks.onSuccess(movieInfoArrayList);
                }
                else{
                    Throwable throwable = new Throwable();
                    callbacks.onFailure(throwable);
                    Log.d("NETWORK ERROR", "MOVIEINFO CALL FAILED");
                }

            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                   callbacks.onFailure(t);
            }
        });
    }

    public void getReviewList(int id, final RepositoryCallbacks<ArrayList<Review>> callbacks){
        Call<ReviewList> call = networkApi.getReviewList(
                id,
                NetworkApi.API_KEY);
        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(@NonNull Call<ReviewList> call, @NonNull Response<ReviewList> response) {
                ReviewList reviewList = response.body();
                ArrayList<Review> reviewArrayList = null;
                if (reviewList != null) {
                    reviewArrayList = reviewList.getReviewList();
                    callbacks.onSuccess(reviewArrayList);
                }
                else{
                    Throwable throwable = new Throwable();
                    callbacks.onFailure(throwable);
                    Log.d("NETWORK ERROR", "REVIEW CALL FAILED");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewList> call, @NonNull Throwable t) {
                    callbacks.onFailure(t);

            }
        });

    }

    public void getTrailerList(int id, final RepositoryCallbacks<ArrayList<Trailer>> callbacks){
        Call<TrailerList> call = networkApi.getTrailerList(
                id,
                NetworkApi.API_KEY);
        call.enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(@NonNull Call<TrailerList> call, @NonNull Response<TrailerList> response) {
                TrailerList trailerList = response.body();
                ArrayList<Trailer> trailerArrayList = null;
                if (trailerList != null) {
                    trailerArrayList = trailerList.getTrailerList();
                    callbacks.onSuccess(trailerArrayList);
                }
                else{
                    Throwable throwable = new Throwable();
                    callbacks.onFailure(throwable);
                    Log.d("NETWORK ERROR", "TRAILER CALL FAILED");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailerList> call, @NonNull Throwable t) {
                    callbacks.onFailure(t);
            }
        });
    }
    public void getAllFavoriteMovies(RepositoryCallbacks<ArrayList<MovieInfo>> callbacks){


        new GetFavoriteMoviesAsyncTask(movieDao, callbacks).execute();
    }

    public void insertFavoriteMovie(MovieInfo movieInfo){
        new InsertFavoriteMovieAsyncTask(movieDao).execute(movieInfo);
    }

    public void deleteFavoritedMovie(MovieInfo movieInfo){
        new DeleteFavoriteMovieAsyncTask(movieDao).execute(movieInfo);
    }



    private static class GetFavoriteMoviesAsyncTask extends AsyncTask<Void, Void, ArrayList<MovieInfo>>{

        final private MovieDao movieDao;
        final private RepositoryCallbacks<ArrayList<MovieInfo>> callbacks;

        GetFavoriteMoviesAsyncTask(MovieDao movieDao, RepositoryCallbacks<ArrayList<MovieInfo>> callbacks){
            this.movieDao = movieDao;
            this.callbacks = callbacks;
        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(Void... voids) {
            return (ArrayList<MovieInfo>) movieDao.getAllFavoriteMovies();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movieInfos) {
            callbacks.onSuccess(movieInfos);
            super.onPostExecute(movieInfos);
        }


    }


    private static class InsertFavoriteMovieAsyncTask extends AsyncTask<MovieInfo, Void, Void>{

        final private MovieDao movieDao;
        InsertFavoriteMovieAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;

        }
        @Override
        protected Void doInBackground(MovieInfo... movieInfos) {
            movieDao.insertFavoriteMovie(movieInfos[0]);
            return null;
        }
    }

    private static class DeleteFavoriteMovieAsyncTask extends AsyncTask<MovieInfo, Void, Void>{

        final private MovieDao movieDao;
        DeleteFavoriteMovieAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;

        }
        @Override
        protected Void doInBackground(MovieInfo... movieInfos) {
            movieDao.deleteFavoriteMovie(movieInfos[0]);
            return null;
        }
    }


}

