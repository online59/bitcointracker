package com.example.bitcoinmarketprice.historyrecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.room.BitcoinPrice;

import java.util.List;

public class HistoricPriceAdapter extends RecyclerView.Adapter<HistoricPriceAdapter.PriceViewHolder> {

    private List<BitcoinPrice> listData;

    public HistoricPriceAdapter() {
    }

    public void setListData(List<BitcoinPrice> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_item, parent, false);
        return new PriceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
        BitcoinPrice item = listData.get(position);
        holder.requestTime.setText(item.getRequestTime());
        holder.priceUsd.setText(item.getUsdRate());
        holder.priceGbp.setText(item.getGbpRate());
        holder.priceEur.setText(item.getEurRate());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class PriceViewHolder extends RecyclerView.ViewHolder{

        private TextView requestTime, priceUsd, priceGbp, priceEur;

        public PriceViewHolder(@NonNull View itemView) {
            super(itemView);
            requestTime = itemView.findViewById(R.id.price_time);
            priceUsd = itemView.findViewById(R.id.price_usd);
            priceGbp = itemView.findViewById(R.id.price_gbp);
            priceEur = itemView.findViewById(R.id.price_eur);
        }

        public TextView getRequestTime() {
            return requestTime;
        }

        public TextView getPriceUsd() {
            return priceUsd;
        }

        public TextView getPriceGbp() {
            return priceGbp;
        }

        public TextView getPriceEur() {
            return priceEur;
        }
    }
}
