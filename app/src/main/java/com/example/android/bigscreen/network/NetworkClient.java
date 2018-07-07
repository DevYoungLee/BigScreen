package com.example.android.bigscreen.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    private static NetworkApi networkApi;

    public static NetworkApi getNetworkApi(){
        if(networkApi == null){
            setUpRetrofit();
        }
        return networkApi;
    }

    private static void setUpRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkApi.MOVIEDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkApi = retrofit.create(NetworkApi.class);

    }


}
