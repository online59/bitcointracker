package com.example.bitcoinmarketprice.retrofit;

import com.example.bitcoinmarketprice.model.BitcoinMeta;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetBitcoinDataApi {

    @GET("v1/bpi/currentprice.json/")
    Call<BitcoinMeta> getBitcoinMetaData();
}
