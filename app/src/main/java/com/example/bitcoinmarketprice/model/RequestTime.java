package com.example.bitcoinmarketprice.model;

import com.google.gson.annotations.SerializedName;

public class RequestTime {

    @SerializedName("updated")
    private String updated;
    @SerializedName("updatedISO")
    private String updatedISO;
    @SerializedName("updateduk")
    private String updateduk;

    public String getUpdated() {
        return updated;
    }

    public String getUpdatedISO() {
        return updatedISO;
    }

    public String getUpdateduk() {
        return updateduk;
    }
}
