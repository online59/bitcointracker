package com.example.bitcoinmarketprice.view;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.util.MyUtils;
import com.example.bitcoinmarketprice.vm.MainViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HistoricPriceAdapter extends RecyclerView.Adapter<HistoricPriceAdapter.PriceViewHolder> {

    private List<BitcoinPrice> listData = new ArrayList<>();

    public HistoricPriceAdapter(MainViewModel viewModel, LifecycleOwner lifecycleOwner) {
        viewModel.getAllPrice().observe(lifecycleOwner, bitcoinPriceList -> {

            if (bitcoinPriceList != null) {
                listData.clear();
                listData.addAll(bitcoinPriceList);
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public HistoricPriceAdapter.PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.historic_price_card, parent, false);
        return new PriceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricPriceAdapter.PriceViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            BitcoinPrice item = listData.get(position);
            holder.getTvRequestTime().setText(MyUtils.getItemTime(item.getRequestTime()));
            holder.getTvRequestPriceUsd().setText(MyUtils.getWholePrice(item.getUsdRate()));
            holder.getTvRequestPriceGbp().setText(MyUtils.getWholePrice(item.getGbpRate()));
            holder.getTvRequestPriceEur().setText(MyUtils.getWholePrice(item.getEurRate()));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public static class PriceViewHolder extends RecyclerView.ViewHolder {


        private TextView tvRequestTime, tvRequestPriceUsd, tvRequestPriceGbp, tvRequestPriceEur;

        public PriceViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRequestTime = itemView.findViewById(R.id.tv_update_time);
            tvRequestPriceUsd = itemView.findViewById(R.id.tv_historic_price_usd);
            tvRequestPriceGbp = itemView.findViewById(R.id.tv_historic_price_gbp);
            tvRequestPriceEur = itemView.findViewById(R.id.tv_historic_price_eur);
        }

        public TextView getTvRequestTime() {
            return tvRequestTime;
        }

        public TextView getTvRequestPriceUsd() {
            return tvRequestPriceUsd;
        }

        public TextView getTvRequestPriceGbp() {
            return tvRequestPriceGbp;
        }

        public TextView getTvRequestPriceEur() {
            return tvRequestPriceEur;
        }
    }
}
