package com.example.bitcoinmarketprice.model;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;


public class PriceMeta {

    @SerializedName("USD")
    private CoinMeta usd;
    @SerializedName("GBP")
    private CoinMeta gbp;
    @SerializedName("EUR")
    private CoinMeta eur;

    public CoinMeta getUsd() {
        return usd;
    }

    public CoinMeta getGbp() {
        return gbp;
    }

    public CoinMeta getEur() {
        return eur;
    }
}
