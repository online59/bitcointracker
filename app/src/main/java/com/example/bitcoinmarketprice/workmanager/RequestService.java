package com.example.bitcoinmarketprice.workmanager;

import static android.content.ContentValues.TAG;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bitcoinmarketprice.api.RetrofitRepository;
import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.database.RoomRepository;
import com.example.bitcoinmarketprice.util.MyUtils;

import java.io.Serializable;

public class RequestService extends IntentService {

    RetrofitRepository retrofitRepository;

    public RequestService() {
        super("RequestServerDataService");
        retrofitRepository = RetrofitRepository.getInstance(getApplication());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BitcoinPrice bitcoinPrice = (BitcoinPrice) intent.getSerializableExtra(MyUtils.INTENT_LAST_ITEM_IN_DATABASE);

        if (bitcoinPrice != null) {
            String previousRequestTime = bitcoinPrice.getRequestTime();
            retrofitRepository.requestBitcoinData(previousRequestTime);
        } else {
            retrofitRepository.requestBitcoinData("");
        }

        Log.e(TAG, "onHandleIntent: Call");
    }
}
