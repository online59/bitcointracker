package com.example.bitcoinmarketprice.workmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start a service or perform some other action when the broadcast is received
        Intent serviceIntent = new Intent(context, RequestService.class);
        context.startService(serviceIntent);
    }
}
