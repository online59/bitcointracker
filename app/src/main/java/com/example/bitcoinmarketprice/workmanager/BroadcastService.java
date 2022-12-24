package com.example.bitcoinmarketprice.workmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.util.MyUtils;

import java.io.Serializable;

public class BroadcastService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start a service or perform some other action when the broadcast is received
        Intent serviceIntent = new Intent(context, RequestService.class);
        Serializable bitcoinPrice = intent.getSerializableExtra(MyUtils.INTENT_LAST_ITEM_IN_DATABASE);

        if (bitcoinPrice != null) {
            serviceIntent.putExtra(MyUtils.INTENT_LAST_ITEM_IN_DATABASE, bitcoinPrice);
        }

        context.startService(serviceIntent);
    }
}
