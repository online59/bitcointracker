package com.example.bitcoinmarketprice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.vm.MainViewModel;

public class CurrentPriceFragment extends Fragment {

    private TextView tvBitcoinInDollar, tvBitcoinInPond, tvBitcoinInEuro;
    MainViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_price, container, false);

        bindView(view);
        setupViewModel();


        return view;
    }

    private void bindView(View view) {
        tvBitcoinInDollar = view.findViewById(R.id.tv_bitcoin_in_dollar);
        tvBitcoinInPond = view.findViewById(R.id.tv_bitcoin_in_pond);
        tvBitcoinInEuro = view.findViewById(R.id.tv_bitcoin_in_euro);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void setupViewModel() {
        viewModel.getLatestBitcoinPrice().observe(getViewLifecycleOwner(), latestData -> {

            // If there is currently no data in database just return
            if (latestData == null) {
                viewModel.getBitcoinMetaDataFromServer().observe(getViewLifecycleOwner(), loadedData -> {

                    tvBitcoinInDollar.setText(loadedData.getBitcoinPrices().getUsd().getRate());
                    tvBitcoinInPond.setText(loadedData.getBitcoinPrices().getGbp().getRate());
                    tvBitcoinInEuro.setText(loadedData.getBitcoinPrices().getEur().getRate());

                    BitcoinPrice price = new BitcoinPrice(loadedData.getRequestTime().getUpdated(),
                            loadedData.getBitcoinPrices().getUsd().getRate(),
                            loadedData.getBitcoinPrices().getGbp().getRate(),
                            loadedData.getBitcoinPrices().getEur().getRate());

                    viewModel.insertNewBitcoinPrice(price);
                });
                return;
            }

            // Update ui
            tvBitcoinInDollar.setText(latestData.getUsdRate());
            tvBitcoinInPond.setText(latestData.getGbpRate());
            tvBitcoinInEuro.setText(latestData.getEurRate());
        });
    }
}