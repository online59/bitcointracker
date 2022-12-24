package com.example.bitcoinmarketprice.api;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.database.CoinDao;
import com.example.bitcoinmarketprice.database.CoinDatabase;
import com.example.bitcoinmarketprice.database.RoomRepository;
import com.example.bitcoinmarketprice.model.BitcoinMeta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRepository {

    private static final String TAG = "RetrofitRepository";

    private static RetrofitRepository instance;
    private RoomRepository roomRepository;
    private RetrofitRepository(Application application) {
        roomRepository = RoomRepository.getInstance(application);
    }

    public static RetrofitRepository getInstance(Application application) {
        if (instance == null) {
            instance = new RetrofitRepository(application);
        }

        return instance;
    }

    public LiveData<BitcoinMeta> requestBitcoinData(String previousRequestTime) {

        GetBitcoinDataApi getBitcoinDataApi = RetrofitClientInstance
                .getRetrofitInstance()
                .create(GetBitcoinDataApi.class);

        Call<BitcoinMeta> sendRequest = getBitcoinDataApi.getBitcoinMetaData();

        // Store data in mutable live data
        final MutableLiveData<BitcoinMeta> bitcoinMetaData = new MutableLiveData<>();

        // Request data on background thread
        sendRequest.enqueue(new Callback<BitcoinMeta>() {
            @Override
            public void onResponse(@NonNull Call<BitcoinMeta> call, @NonNull Response<BitcoinMeta> response) {

                // Check if task is successful
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                BitcoinMeta meta = response.body();

                // If request is successful, store new incoming data in bitcoinMetaData
                bitcoinMetaData.setValue(meta);

                BitcoinPrice price = new BitcoinPrice(meta.getRequestTime().getUpdated(),
                        meta.getBitcoinPrices().getUsd().getRate(),
                        meta.getBitcoinPrices().getGbp().getRate(),
                        meta.getBitcoinPrices().getEur().getRate());

                // Check whether this data is already added to the database
                if (!previousRequestTime.equalsIgnoreCase(meta.getRequestTime().getUpdated())) {
                    // After data is loaded, add it to room database
                    roomRepository.insertNewPrice(price);
                }

            }

            @Override
            public void onFailure(@NonNull Call<BitcoinMeta> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                bitcoinMetaData.setValue(null);

            }
        });

        return bitcoinMetaData;
    }
}
