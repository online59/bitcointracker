package com.example.bitcoinmarketprice.model;

import com.google.gson.annotations.SerializedName;

public class CoinMeta {

    @SerializedName("code")
    private String currency;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("rate")
    private String rate;
    @SerializedName("description")
    private String description;
    @SerializedName("rate_float")
    private float rate_float;

    public String getCurrency() {
        return currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public float getRate_float() {
        return rate_float;
    }
}
