package com.example.bitcoinmarketprice.historyrecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.room.BitcoinPrice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HistoricPriceAdapter extends RecyclerView.Adapter<HistoricPriceAdapter.PriceViewHolder> {

    private List<BitcoinPrice> listData;

    public HistoricPriceAdapter() {
    }

    public void setListData(List<BitcoinPrice> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public HistoricPriceAdapter.PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_item, parent, false);
        return new PriceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricPriceAdapter.PriceViewHolder holder, int position) {
        BitcoinPrice item = listData.get(position);
        holder.requestTime.setText(getItemTime(item.getRequestTime()));
        holder.priceUsd.setText(item.getUsdRate());
        holder.priceGbp.setText(item.getGbpRate());
        holder.priceEur.setText(item.getEurRate());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private String getItemTime(String itemDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss 'UTC'", Locale.US);
        Date date;
        try {
            date = inputFormat.parse(itemDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy KK:mm a", Locale.US);
        outputFormat.setTimeZone(TimeZone.getTimeZone("Thailand/Bangkok"));
        // Convert to Thailand time zone
        final long millisToAdd = 50_400_000;
        date.setTime(date.getTime() + millisToAdd);
        return outputFormat.format(date);
    }


    public static class PriceViewHolder extends RecyclerView.ViewHolder {

        private final TextView requestTime;
        private final TextView priceUsd;
        private final TextView priceGbp;
        private final TextView priceEur;

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
