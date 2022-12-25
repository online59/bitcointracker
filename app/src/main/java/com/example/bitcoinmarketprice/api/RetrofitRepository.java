package com.example.bitcoinmarketprice.api;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bitcoinmarketprice.database.BitcoinPrice;
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

    public LiveData<BitcoinMeta> requestBitcoinData() {

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

                BitcoinMeta bitcoinMeta = response.body();

                // If request is successful, store new incoming data in bitcoinMetaData
                bitcoinMetaData.setValue(bitcoinMeta);

                BitcoinPrice bitcoinPrice = new BitcoinPrice();
                bitcoinPrice.setRequestTime(bitcoinMeta.getRequestTime().getUpdated());
                bitcoinPrice.setUsdRate(bitcoinMeta.getBitcoinPrices().getUsd().getRate());
                bitcoinPrice.setGbpRate(bitcoinMeta.getBitcoinPrices().getGbp().getRate());
                bitcoinPrice.setEurRate(bitcoinMeta.getBitcoinPrices().getEur().getRate());

                roomRepository.insertNewPrice(bitcoinPrice);

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
