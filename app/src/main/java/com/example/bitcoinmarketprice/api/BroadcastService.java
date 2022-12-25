package com.example.bitcoinmarketprice.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bitcoinmarketprice.util.MyUtils;

public class BroadcastService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start a service or perform some other action when the broadcast is received
        Intent serviceIntent = new Intent(context, RequestService.class);
        context.startService(serviceIntent);
    }
}
