package com.example.bitcoinmarketprice.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitcoinmarketprice.R;
import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.vm.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class PriceCardPagerAdapter extends RecyclerView.Adapter<PriceCardPagerAdapter.PagerViewHolder> {

    private static final int CURRENCY_COUNT = 3;
    private List<PagerModel> pagerDataList = new ArrayList<>();

    public PriceCardPagerAdapter(MainViewModel viewModel, LifecycleOwner lifecycleOwner) {
        viewModel.getLatestPrice().observe(lifecycleOwner, lastUpdateData -> {
            String updateTime = lastUpdateData.getRequestTime();
            String updatePriceUsd = lastUpdateData.getUsdRate();
            String updatePriceGbp = lastUpdateData.getGbpRate();
            String updatePriceEur = lastUpdateData.getEurRate();

            String[] listItem = {updatePriceUsd, updatePriceGbp, updatePriceEur};

            for (String s : listItem) {
                pagerDataList.add(new PagerModel(0, updateTime, s));
            }

            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public PriceCardPagerAdapter.PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_price_card, parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceCardPagerAdapter.PagerViewHolder holder, int position) {
        PagerModel item = pagerDataList.get(position);
        holder.getIvCurrency().setImageResource(R.drawable.bottom_app_icon_menu_1);
        holder.getTvUpdateTime().setText(item.getTvUpdateTime());
        holder.getTvUpdatePrice().setText(item.getTvUpdatePrice());
    }

    @Override
    public int getItemCount() {
        return pagerDataList == null ? 0 : pagerDataList.size();
    }

    public static class PagerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivCurrency;
        private final TextView tvUpdateTime, tvUpdatePrice;

        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCurrency = itemView.findViewById(R.id.iv_currency);
            tvUpdateTime = itemView.findViewById(R.id.tv_update_time);
            tvUpdatePrice = itemView.findViewById(R.id.tv_update_price);
        }

        public ImageView getIvCurrency() {
            return ivCurrency;
        }

        public TextView getTvUpdateTime() {
            return tvUpdateTime;
        }

        public TextView getTvUpdatePrice() {
            return tvUpdatePrice;
        }
    }

    public static class PagerModel {
        private final int ivCurrency;
        private final String tvUpdateTime, tvUpdatePrice;

        public PagerModel(int ivCurrency, String tvUpdateTime, String tvUpdatePrice) {
            this.ivCurrency = ivCurrency;
            this.tvUpdateTime = tvUpdateTime;
            this.tvUpdatePrice = tvUpdatePrice;
        }

        public int getIvCurrency() {
            return ivCurrency;
        }

        public String getTvUpdateTime() {
            return tvUpdateTime;
        }

        public String getTvUpdatePrice() {
            return tvUpdatePrice;
        }
    }
}
