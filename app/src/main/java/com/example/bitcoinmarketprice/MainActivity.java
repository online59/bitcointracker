package com.example.bitcoinmarketprice;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.bitcoinmarketprice.room.CoinDao;
import com.example.bitcoinmarketprice.room.CoinDatabase;
import com.example.bitcoinmarketprice.vm.MainViewModel;
import com.example.bitcoinmarketprice.workmanager.NetworkConstraint;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView textResult;

    // Set up fragment and bottom navigation bar
    private BottomNavigationView navigationView;
    private Bundle message;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, CurrentPriceFragment.class, message)
                    .commit();
        }

        bindView();
        bottomAppBarSetup();
    }

    private void bottomAppBarSetup() {

        navigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_current_price) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, CurrentPriceFragment.class, message)
                        .commit();
                return true;
            } else if (id == R.id.action_price_history) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, HistoryPriceFragment.class, message)
                        .commit();
                return true;
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, BitcoinConverterFragment.class, message)
                        .commit();
                return true;
            }
        });
    }

    private void bindView() {
        navigationView = findViewById(R.id.bottom_navigation_view);

        // Start requesting bitcoin meta data
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Delete bitcoin data in memory
        viewModel.deleteAll();

        // Fetch new data from server
        viewModel.fetchDataOnce();
    }
}