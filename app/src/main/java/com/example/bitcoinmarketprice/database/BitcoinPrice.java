package com.example.bitcoinmarketprice.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BitcoinPrice {

    public BitcoinPrice(String requestTime, String usdRate, String gbpRate, String eurRate) {
        this.requestTime = requestTime;
        this.usdRate = usdRate;
        this.gbpRate = gbpRate;
        this.eurRate = eurRate;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "request_time")
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
