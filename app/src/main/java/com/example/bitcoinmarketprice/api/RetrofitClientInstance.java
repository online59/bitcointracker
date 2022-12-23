package com.example.bitcoinmarketprice.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.coindesk.com/";

    // Create instance of retrofit
    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            // If no instance of retrofit is currently created, then create new
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // Return retrofit object
        return retrofit;
    }
}
