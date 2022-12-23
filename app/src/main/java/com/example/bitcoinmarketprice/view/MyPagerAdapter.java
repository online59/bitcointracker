package com.example.bitcoinmarketprice.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitcoinmarketprice.R;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends RecyclerView.Adapter<MyPagerAdapter.PagerViewHolder>{

    private List<PagerModel> pagerDataList = new ArrayList<>();

    public MyPagerAdapter(List<PagerModel> pagerDataList) {
        this.pagerDataList = pagerDataList;
    }

    @NonNull
    @Override
    public MyPagerAdapter.PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_price_card, parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPagerAdapter.PagerViewHolder holder, int position) {
        PagerModel item = pagerDataList.get(position);
        holder.getIvCurrency().setImageResource(item.ivCurrency);
        holder.getTvUpdateTime().setText(item.tvUpdateTime);
        holder.getTvUpdatePrice().setText(item.tvUpdatePrice);
    }

    @Override
    public int getItemCount() {
        return pagerDataList == null ? 0 : pagerDataList.size();
    }

    public static class PagerViewHolder extends RecyclerView.ViewHolder{

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
        private int ivCurrency;
        private String tvUpdateTime, tvUpdatePrice;

        public PagerModel() {
        }

        public PagerModel(int ivCurrency, String tvUpdateTime, String tvUpdatePrice) {
            this.ivCurrency = ivCurrency;
            this.tvUpdateTime = tvUpdateTime;
            this.tvUpdatePrice = tvUpdatePrice;
        }

        public int getIvCurrency() {
            return ivCurrency;
        }

        public void setIvCurrency(int ivCurrency) {
            this.ivCurrency = ivCurrency;
        }

        public String getTvUpdateTime() {
            return tvUpdateTime;
        }

        public void setTvUpdateTime(String tvUpdateTime) {
            this.tvUpdateTime = tvUpdateTime;
        }

        public String getTvUpdatePrice() {
            return tvUpdatePrice;
        }

        public void setTvUpdatePrice(String tvUpdatePrice) {
            this.tvUpdatePrice = tvUpdatePrice;
        }
    }
}
