package com.example.bitcoinmarketprice.workmanager;

import static android.content.ContentValues.TAG;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bitcoinmarketprice.api.RetrofitRepository;
import com.example.bitcoinmarketprice.database.RoomRepository;

public class RequestService extends IntentService {

    RetrofitRepository retrofitRepository;

    public RequestService() {
        super("RequestServerDataService");
        retrofitRepository = RetrofitRepository.getInstance(getApplication());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        retrofitRepository.requestBitcoinData();
        Log.e(TAG, "onHandleIntent: Call");
    }
}
