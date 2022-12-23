package com.example.bitcoinmarketprice.view;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.vm.MainViewModel;
import com.example.bitcoinmarketprice.workmanager.SyncDataWorker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SyncDataWorker.SyncDataWorkerCallback {

    // Set up fragment and bottom navigation bar
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        loadData();
        setRecyclerView();
    }

    private void loadData() {
        viewModel.requestBitcoinData(this);
    }

    private void addData(BitcoinPrice updatePrice) {
        viewModel.getLatestPrice().observe(this, previousPrice -> {

            if (previousPrice == null) {
                return;
            }
            String previousRequestTime = previousPrice.getRequestTime();

            String updateRequestTime = updatePrice.getRequestTime();

            if (!previousRequestTime.equalsIgnoreCase(updateRequestTime)) {
                viewModel.insertNewPrice(updatePrice);
            }
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