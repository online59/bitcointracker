package com.example.bitcoinmarketprice.workmanager;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.bitcoinmarketprice.api.RetrofitRepository;

public class RequestService extends IntentService {

    RetrofitRepository retrofitRepository;

    public RequestService() {
        super("RequestServerDataService");
        retrofitRepository = RetrofitRepository.getInstance();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        retrofitRepository.requestBitcoinData();
    }
}
