package com.example.bitcoinmarketprice.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


public class BitcoinMeta {

    @SerializedName("time")
    private RequestTime requestTime;

    @SerializedName("disclaimer")
    private String disclaimer;


    @SerializedName("chartName")
    private String coinName;

    @SerializedName("bpi")
    private PriceMeta pricesMeta;

    public RequestTime getRequestTime() {
        return requestTime;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getCoinName() {
        return coinName;
    }

    public PriceMeta getBitcoinPrices() {
        return pricesMeta;
    }

    public void setRequestTime(RequestTime requestTime) {
        this.requestTime = requestTime;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public void setPricesMeta(PriceMeta pricesMeta) {
        this.pricesMeta = pricesMeta;
    }
}
