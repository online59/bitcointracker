package com.example.bitcoinmarketprice.workmanager;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.retrofit.GetBitcoinDataApi;
import com.example.bitcoinmarketprice.retrofit.RetrofitClientInstance;
import com.example.bitcoinmarketprice.retrofit.RetrofitRepository;
import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.room.RoomRepository;
import com.example.bitcoinmarketprice.vm.MainViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataWorker extends Worker {

    RetrofitRepository retrofitRepository;
    RoomRepository roomRepository;
    MainViewModel viewModel;

    private static final String TAG = SyncDataWorker.class.getSimpleName();

    public SyncDataWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        retrofitRepository = new RetrofitRepository();
        roomRepository = new RoomRepository((Application) appContext);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) appContext).get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Result doWork() {

        //simulate slow work
        // WorkerUtils.makeStatusNotification("Fetching Data", applicationContext);
        Log.i(TAG, "Fetching Data from Remote host");

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

                // If request is successful, store new incoming data in bitcoinMetaData
                bitcoinMetaData.setValue(response.body());
                notifyWhenDataReady(bitcoinMetaData.getValue());
            }

            @Override
            public void onFailure(@NonNull Call<BitcoinMeta> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                bitcoinMetaData.setValue(null);

            }
        });
    }

    private void insetNewBitcoinPrice(BitcoinMeta meta) {

        BitcoinPrice bitcoinPrice = new BitcoinPrice(meta.getRequestTime().getUpdated(),
                meta.getBitcoinPrices().getUsd().getRate(),
                meta.getBitcoinPrices().getGbp().getRate(),
                meta.getBitcoinPrices().getEur().getRate());

        viewModel.getLatestBitcoinPrice().observe((LifecycleOwner) getApplicationContext(), latestItem -> {
            String latestUpdateTime = latestItem.getRequestTime();
            String newItemRequestTime = meta.getRequestTime().getUpdated();

            if (!newItemRequestTime.equals(latestUpdateTime)) {
                roomRepository.insertNewBitcoinPrice(bitcoinPrice);
            }
        });

    }

    private void notifyWhenDataReady(BitcoinMeta bitcoinMeta) {
        // When data is loaded, insert to room database
        insetNewBitcoinPrice(bitcoinMeta);
        // Recursive, keep calling itself
        NetworkConstraint.getInstance(getApplicationContext()).fetchDataOnce();
    }


    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this worker");
    }
}
