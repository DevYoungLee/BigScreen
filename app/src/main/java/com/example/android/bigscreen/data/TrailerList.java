package com.example.android.bigscreen.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrailerList {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<Trailer> trailerList = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(ArrayList<Trailer> results) {
        this.trailerList = results;
    }
}
