package com.example.bitcoinmarketprice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.vm.MainViewModel;

public class BitcoinConverterFragment extends Fragment {

    private TextView tvUsd, tvGbp, tvEur;
    private EditText edtCurrency;
    private Button btnConvert;
    private BitcoinPrice bitcoinPrice;

    public BitcoinConverterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bitcoin_converter, container, false);

        bindView(view);
        getLatestRate();

        return view;
    }

    private void getLatestRate() {
        MainViewModel viewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
        viewModel.getLatestBitcoinPrice(getContext()).observe(getViewLifecycleOwner(), meta -> {
            bitcoinPrice = meta;

            // Set up click event
            btnConvert.setOnClickListener(view -> handlingClickEvents());
        });
    }

    private void handlingClickEvents() {

        String usd, gbp, eur;
        float currentUsdRate, currentGbpRate, currentEurRate;

        currentUsdRate = Float.parseFloat(bitcoinPrice.getUsdRate().replace(",", ""));
        currentGbpRate = Float.parseFloat(bitcoinPrice.getGbpRate().replace(",", ""));
        currentEurRate = Float.parseFloat(bitcoinPrice.getEurRate().replace(",", ""));

        usd = edtCurrency.getText().toString().trim();
        gbp = edtCurrency.getText().toString().trim();
        eur = edtCurrency.getText().toString().trim();


        if (usd.equals("")) {
            usd = "-";
        } else {
            usd = String.valueOf(Float.parseFloat(usd) / currentUsdRate);
        }

        if (gbp.equals("")) {
            gbp = "-";
        } else {
            gbp = String.valueOf(Float.parseFloat(gbp) / currentGbpRate);
        }

        if  (eur.equals("")){
            eur = "-";
        } else {
            eur = String.valueOf(Float.parseFloat(eur) / currentEurRate);
        }

        tvUsd.setText(usd);
        tvGbp.setText(gbp);
        tvEur.setText(eur);

    }



    private void bindView(View view) {
        tvUsd = view.findViewById(R.id.tv_usd);
        tvGbp = view.findViewById(R.id.tv_gbp);
        tvEur = view.findViewById(R.id.tv_eur);

        edtCurrency = view.findViewById(R.id.edt_bitcoin_to_currency);

        btnConvert = view.findViewById(R.id.btn_convert);
    }
}