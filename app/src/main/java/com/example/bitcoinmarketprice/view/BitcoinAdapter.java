package com.example.bitcoinmarketprice.view;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.util.MyUtils;
import com.example.bitcoinmarketprice.vm.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class BitcoinAdapter extends RecyclerView.Adapter<BitcoinAdapter.BitcoinCardViewHolder> {

    private final static int CURRENCY_COUNT = 3;
    private final List<BitcoinCardModel> dataList = new ArrayList<>();

    public BitcoinAdapter(MainViewModel viewModel, LifecycleOwner lifecycleOwner) {
        viewModel.getLatestPrice().observe(lifecycleOwner, updatedData -> {
            if (updatedData != null && updatedData.size() > 0) {
                dataList.clear(); // Clear old data before add new data

                String[] currencyName = {"US Dollar", "Pound", "Euro"};
                String[] currentRate = {updatedData.get(0).getUsdRate(), updatedData.get(0).getGbpRate(), updatedData.get(0).getEurRate()};
                String[] previousRate = null;
                int[] currencySymbol = {R.drawable.usd_symbol, R.drawable.gbp_symbol, R.drawable.eur_symbol};

                if (updatedData.size() > 1) {
                    previousRate = new String[]{updatedData.get(1).getUsdRate(), updatedData.get(1).getGbpRate(), updatedData.get(1).getEurRate()};
                    Log.e(TAG, "BitcoinAdapter: " + updatedData.size());
                }

                Log.e(TAG, "BitcoinAdapter: " + updatedData.size());

                for (int i = 0; i < CURRENCY_COUNT; i++) {
                    if (previousRate != null) {
                        dataList.add(new BitcoinCardModel(currencySymbol[i],
                                currencyName[i],
                                currentRate[i],
                                updatedData.get(0).getRequestTime(),
                                MyUtils.getPercentageChange(currentRate[i], previousRate[i])));
                        Log.e(TAG, "BitcoinAdapter: not null");
                    } else {
                        dataList.add(new BitcoinCardModel(currencySymbol[i],
                                currencyName[i],
                                currentRate[i],
                                updatedData.get(0).getRequestTime(),
                                MyUtils.getPercentageChange(currentRate[i], null)));
                        Log.e(TAG, "BitcoinAdapter: null");
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public BitcoinCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bitcoin_value, parent, false);
        return new BitcoinCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BitcoinCardViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            BitcoinCardModel item = dataList.get(position);
            holder.getIvCurrency().setImageResource(item.getIvCurrency());
            holder.getTvCurrencyName().setText(item.getTvCurrencyName());
            holder.getTvCurrentRate().setText(item.getTvCurrentRate());
            holder.getTvUpdateTime().setText(item.getTvUpdateTime());
            holder.getTvPercentChange().setText(item.getTvPercentChange());
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public static class BitcoinCardViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivCurrency;
        private final TextView tvCurrencyName, tvCurrentRate, tvUpdateTime, tvPercentChange;

        public BitcoinCardViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCurrency = itemView.findViewById(R.id.iv_currency);
            tvCurrencyName = itemView.findViewById(R.id.tv_currency_symbol);
            tvCurrentRate = itemView.findViewById(R.id.tv_current_rate);
            tvUpdateTime = itemView.findViewById(R.id.tv_update_time);
            tvPercentChange = itemView.findViewById(R.id.tv_percent_change);
        }

        public ImageView getIvCurrency() {
            return ivCurrency;
        }

        public TextView getTvCurrencyName() {
            return tvCurrencyName;
        }

        public TextView getTvCurrentRate() {
            return tvCurrentRate;
        }

        public TextView getTvUpdateTime() {
            return tvUpdateTime;
        }

        public TextView getTvPercentChange() {
            return tvPercentChange;
        }
    }

    public static class BitcoinCardModel {

        private int ivCurrency;
        private String tvCurrencyName, tvCurrentRate, tvUpdateTime, tvPercentChange;

        public BitcoinCardModel() {
        }

        public BitcoinCardModel(int ivCurrency, String tvCurrencyName, String tvCurrentRate, String tvUpdateTime, String tvPercentChange) {
            this.ivCurrency = ivCurrency;
            this.tvCurrencyName = tvCurrencyName;
            this.tvCurrentRate = tvCurrentRate;
            this.tvUpdateTime = tvUpdateTime;
            this.tvPercentChange = tvPercentChange;
        }

        public int getIvCurrency() {
            return ivCurrency;
        }

        public String getTvCurrencyName() {
            return tvCurrencyName;
        }

        public String getTvCurrentRate() {
            return tvCurrentRate;
        }

        public String getTvUpdateTime() {
            return tvUpdateTime;
        }

        public String getTvPercentChange() {
            return tvPercentChange;
        }
    }
}
