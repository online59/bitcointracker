package com.example.bitcoinmarketprice.view;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.api.BroadcastService;
import com.example.bitcoinmarketprice.vm.MainViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    // Set up fragment and bottom navigation bar
    MainViewModel viewModel;

    private static final int UPDATE_INTERVAL = 60 * 1000; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup app bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        bindView();
        requestDataPeriodically();
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), getLifecycle()));

        String[] tabName = {"Bitcoin", "Historic"};
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                tab.setText(tabName[position]);
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.menu_calculator) {
            Intent intent = new Intent(this, Bitcoin.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestDataPeriodically() {
        Log.e(TAG, "requestDataPeriodically: called");

        // Create intent for intent server
        Intent intent = new Intent(this, BroadcastService.class);

        // Create a PendingIntent to trigger the IntentService
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get an instance of AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the alarm to repeat at a fixed interval
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), UPDATE_INTERVAL, pendingIntent);
    }

    private void bindView() {

        // Start requesting bitcoin meta data
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Delete bitcoin data in database
        viewModel.deleteAll();

        // Load new data
        viewModel.requestBitcoinData();

    }
}