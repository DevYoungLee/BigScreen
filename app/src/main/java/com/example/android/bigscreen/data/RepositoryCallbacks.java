package com.example.android.bigscreen.data;



public interface RepositoryCallbacks<T> {
    void onSuccess(T t);
    void onFailure(Throwable throwable);

}
