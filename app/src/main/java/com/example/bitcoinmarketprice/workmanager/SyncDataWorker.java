package com.example.bitcoinmarketprice.workmanager;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.retrofit.GetBitcoinDataApi;
import com.example.bitcoinmarketprice.retrofit.RetrofitClientInstance;
import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.room.RoomRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataWorker extends Worker {
    private final RoomRepository roomRepository;
    private static final String TAG = SyncDataWorker.class.getSimpleName();

    public SyncDataWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        roomRepository = new RoomRepository((Application) appContext);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            //create a call to network
            requestBitcoinMetaData();
            return Result.success();

        } catch (Throwable e) {
            e.printStackTrace();
            Log.e(TAG, "Error fetching data", e);
            return Result.failure();
        }
    }

    private void requestBitcoinMetaData() {

        GetBitcoinDataApi getBitcoinDataApi = RetrofitClientInstance
                .getRetrofitInstance()
                .create(GetBitcoinDataApi.class);

        Call<BitcoinMeta> sendRequest = getBitcoinDataApi.getBitcoinMetaData();

        // Request data on background thread
        sendRequest.enqueue(new Callback<BitcoinMeta>() {
            @Override
            public void onResponse(@NonNull Call<BitcoinMeta> call, @NonNull Response<BitcoinMeta> response) {

                // Check if task is successful
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                // If request is successful
                notifyWhenDataReady(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<BitcoinMeta> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                notifyWhenDataReady(null);
            }
        });
    }

    private void notifyWhenDataReady(@Nullable BitcoinMeta bitcoinMeta) {

        if (bitcoinMeta == null) {
            Log.e(TAG, "SyncDataWorker/notifyWhenDataReady: Bitcoin meta data is null");
            return;
        }

        BitcoinPrice bitcoinPrice = new BitcoinPrice(bitcoinMeta.getRequestTime().getUpdated(),
                bitcoinMeta.getBitcoinPrices().getUsd().getRate(),
                bitcoinMeta.getBitcoinPrices().getGbp().getRate(),
                bitcoinMeta.getBitcoinPrices().getEur().getRate());

        // Check condition and insert new bitcoin price if it meet the criteria
        roomRepository.checkInsertNewBitcoinPrice(bitcoinPrice);

        // Recursive, keep calling itself
        NetworkConstraint.getInstance(getApplicationContext()).fetchDataOnce();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this worker");
    }
}
