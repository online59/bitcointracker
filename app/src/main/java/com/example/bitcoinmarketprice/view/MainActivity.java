package com.example.bitcoinmarketprice.view;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.util.MyUtils;
import com.example.bitcoinmarketprice.vm.MainViewModel;
import com.example.bitcoinmarketprice.api.BroadcastService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    // Set up fragment and bottom navigation bar
    MainViewModel viewModel;
    BitcoinPrice bitcoinPrice;

    private static final int UPDATE_INTERVAL = 60 * 1000; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        loadData();
        setRecyclerView();
    }

    private void requestDataPeriodically(String previousRequestTime) {
        Log.e(TAG, "requestDataPeriodically: called");

        // Create intent for intent server
        Intent intent = new Intent(this, BroadcastService.class);

        // Put extra information to intent
        intent.putExtra(MyUtils.INTENT_PREVIOUS_REQUEST_TIME, previousRequestTime);

        // Create a PendingIntent to trigger the IntentService
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get an instance of AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the alarm to repeat at a fixed interval
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), UPDATE_INTERVAL, pendingIntent);
    }

    private void loadData() {

        // This method will be called once
        viewModel.requestBitcoinData(this).observe(this, loadedData -> {
            Log.e(TAG, "loadData: requestBitcoinData called");
            bitcoinPrice = new BitcoinPrice(loadedData.getRequestTime().getUpdated(),
                    loadedData.getBitcoinPrices().getUsd().getRate(),
                    loadedData.getBitcoinPrices().getGbp().getRate(),
                    loadedData.getBitcoinPrices().getEur().getRate());

            // This method will be called first time when new requested data arrived
            // and will continue receive call back every time a new data is updated
            requestDataPeriodically(loadedData.getRequestTime().getUpdated());
        });
    }

    private void setRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        HistoricPriceAdapter adapter = new HistoricPriceAdapter(viewModel, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void bindView() {

        // Start requesting bitcoin meta data
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Delete bitcoin data in database
        viewModel.deleteAll();

        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new PriceCardPagerAdapter(viewModel, this));
    }
}