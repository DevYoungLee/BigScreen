package com.example.android.bigscreen.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.android.bigscreen.data.MovieInfo;
import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movieinfo")
    List<MovieInfo> getAllFavoriteMovies();

    @Insert
    void insertFavoriteMovie(MovieInfo movieInfo);

    @Delete
    void deleteFavoriteMovie(MovieInfo movieInfo);


}
