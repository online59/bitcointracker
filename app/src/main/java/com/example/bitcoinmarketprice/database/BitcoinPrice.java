package com.example.bitcoinmarketprice.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class BitcoinPrice implements Serializable {

    public BitcoinPrice() {
    }

    //    @PrimaryKey(autoGenerate = true)
    private int uid;

    @PrimaryKey
    @ColumnInfo(name = "request_time")
    @NonNull
    private String requestTime;

    @ColumnInfo(name = "usd_rate")
    private String usdRate;

    @ColumnInfo(name = "gbp_rate")
    private String gbpRate;

    @ColumnInfo(name = "eur_rate")
    private String eurRate;



    public int getUid() {
        return uid;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getUsdRate() {
        return usdRate;
    }

    public String getGbpRate() {
        return gbpRate;
    }

    public String getEurRate() {
        return eurRate;
    }


    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setUsdRate(String usdRate) {
        this.usdRate = usdRate;
    }

    public void setGbpRate(String gbpRate) {
        this.gbpRate = gbpRate;
    }

    public void setEurRate(String eurRate) {
        this.eurRate = eurRate;
    }
}
