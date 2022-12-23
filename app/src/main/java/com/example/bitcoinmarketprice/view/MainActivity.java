package com.example.bitcoinmarketprice.view;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.vm.MainViewModel;
import com.example.bitcoinmarketprice.workmanager.BroadcastService;
import com.example.bitcoinmarketprice.workmanager.RequestService;
import com.example.bitcoinmarketprice.workmanager.SyncDataWorker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SyncDataWorker.SyncDataWorkerCallback {

    // Set up fragment and bottom navigation bar
    MainViewModel viewModel;

    private static final int UPDATE_INTERVAL = 60 * 1000; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        loadData();
        setRecyclerView();
        requestDataPeriodically();
    }

    private void requestDataPeriodically() {
        // Create intent for intent server
        Intent intent = new Intent(this, BroadcastService.class);

        // Create a PendingIntent to trigger the IntentService
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get an instance of AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the alarm to repeat at a fixed interval
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), UPDATE_INTERVAL, pendingIntent);
    }

    private void loadData() {
        viewModel.requestBitcoinData(this);
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

        // If data is found, fetch for another new data
//        viewModel.fetchDataOnce();

        List<MyPagerAdapter.PagerModel> dataList = createData();

        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new MyPagerAdapter(dataList));
    }

    private List<MyPagerAdapter.PagerModel> createData() {
        List<MyPagerAdapter.PagerModel> pagerModels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pagerModels.add(new MyPagerAdapter.PagerModel(R.drawable.bottom_app_icon_menu_1, "23 December 2022 10:40 PM", "17,580"));
        }
        return pagerModels;
    }

    @Override
    public void onNewDataLoaded(BitcoinPrice bitcoinPrice) {
        Log.e(TAG, "onNewDataLoaded: " + bitcoinPrice );
    }
}