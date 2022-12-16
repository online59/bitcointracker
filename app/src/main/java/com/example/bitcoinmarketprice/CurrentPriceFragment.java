package com.example.bitcoinmarketprice;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bitcoinmarketprice.util.MessageType;
import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.vm.MainViewModel;
import com.example.bitcoinmarketprice.workmanager.NetworkConstraint;

public class CurrentPriceFragment extends Fragment {

    private TextView tvBitcoinInDollar, tvBitcoinInPond, tvBitcoinInEuro;
    private String message;
    private BitcoinMeta bitcoinMeta;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(MessageType.MESSAGE);
        }
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

    private void reloadUi() {
        FragmentTransaction fragment = getParentFragmentManager().beginTransaction();
        fragment.setReorderingAllowed(false);
        fragment.detach(this).attach(this).commit();
    }

    private void bindView(View view) {
        tvBitcoinInDollar = view.findViewById(R.id.tv_bitcoin_in_dollar);
        tvBitcoinInPond = view.findViewById(R.id.tv_bitcoin_in_pond);
        tvBitcoinInEuro = view.findViewById(R.id.tv_bitcoin_in_euro);
    }

    private void setupViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        viewModel.getLatestBitcoinPrice(getContext()).observe(getViewLifecycleOwner(), bitcoinPrice -> {

            // If there is currently no data in database just return
            if (bitcoinPrice == null) {
                viewModel.getBitcoinMetaData(getContext(), getViewLifecycleOwner()).observe(getViewLifecycleOwner(), bitcoinMeta -> {

                    tvBitcoinInDollar.setText(bitcoinMeta.getBitcoinPrices().getUsd().getRate());
                    tvBitcoinInPond.setText(bitcoinMeta.getBitcoinPrices().getGbp().getRate());
                    tvBitcoinInEuro.setText(bitcoinMeta.getBitcoinPrices().getEur().getRate());

                    BitcoinPrice price = new BitcoinPrice(bitcoinMeta.getRequestTime().getUpdated(),
                            bitcoinMeta.getBitcoinPrices().getUsd().getRate(),
                            bitcoinMeta.getBitcoinPrices().getGbp().getRate(),
                            bitcoinMeta.getBitcoinPrices().getEur().getRate());

                    viewModel.insertNewBitcoinPrice(getContext(), price);

                    reloadUi();
                });
                return;
            }

            // Update ui
            tvBitcoinInDollar.setText(bitcoinPrice.getUsdRate());
            tvBitcoinInPond.setText(bitcoinPrice.getGbpRate());
            tvBitcoinInEuro.setText(bitcoinPrice.getEurRate());

            reloadUi();
        });
    }
}