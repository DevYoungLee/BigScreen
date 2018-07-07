package com.example.android.bigscreen.network;

import com.example.android.bigscreen.data.MovieList;
import com.example.android.bigscreen.data.ReviewList;
import com.example.android.bigscreen.data.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface NetworkApi {
    String API_KEY = "**** INSERT API CODE HERE ****";
    String SORT_BY_POPULARITY = "popular";
    String SORT_BY_RATING = "top_rated";
    String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";
    String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/";
    String PARAM_API_KEY = "api_key";
    String PARAM_LANGUAGE = "language";
    String LANGUAGE = "en-US";
    String PARAM_PAGE = "page";

    @GET("movie/{sortBy}")
     Call<MovieList> getMovieList (@Path("sortBy") String sortBy,
                                   @Query("api_key") String apiKey,
                                   @Query("language") String language,
                                   @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<TrailerList> getTrailerList (@Path("id") int id,
                                    @Query("api_key") String apiKey);


    @GET("movie/{id}/reviews")
    Call<ReviewList> getReviewList (@Path("id") int id,
                                   @Query("api_key") String apiKey);
}




