package com.example.bitcoinmarketprice.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.api.RetrofitRepository;
import com.example.bitcoinmarketprice.database.BitcoinPrice;
import com.example.bitcoinmarketprice.database.RoomRepository;
import com.example.bitcoinmarketprice.workmanager.NetworkConstraint;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    RetrofitRepository retrofitRepository;
    RoomRepository roomRepository;
    NetworkConstraint constraint;

    public MainViewModel(@NonNull Application application) {
        super(application);
        constraint = NetworkConstraint.getInstance(application);
        retrofitRepository = RetrofitRepository.getInstance(application);
        roomRepository = RoomRepository.getInstance(application);
    }

    /**
     * This method request bitcoin price meta data from server and return in java's POJO class
     */
    public LiveData<BitcoinMeta> requestBitcoinData(LifecycleOwner lifecycleOwner) {

//        final MutableLiveData<BitcoinMeta> bitcoinPriceMutableLiveData = new MutableLiveData<>();
//
//        retrofitRepository.requestBitcoinData().observe(lifecycleOwner, loadedData -> {
//
//            BitcoinPrice price = new BitcoinPrice(loadedData.getRequestTime().getUpdated(),
//                    loadedData.getBitcoinPrices().getUsd().getRate(),
//                    loadedData.getBitcoinPrices().getGbp().getRate(),
//                    loadedData.getBitcoinPrices().getEur().getRate());
//
//            roomRepository.insertNewPrice(price);
//
//            bitcoinPriceMutableLiveData.setValue(loadedData);
//        });

        return retrofitRepository.requestBitcoinData();
    }

    /**
     * This method retrieve bitcoin historic price data from BitcoinMeta table and return in java's POJO class
     */
    public LiveData<List<BitcoinPrice>> getAllPrice() {
        return roomRepository.getAllPrice();
    }

    /**
     * This method retrieve latest bitcoin data from table
     */
    public LiveData<BitcoinPrice> getLatestPrice() {
        return roomRepository.getLatestPrice();
    }

    /**
     * This method insert new updated bitcoin price to table
     */
    public void insertNewPrice(BitcoinPrice bitcoinPrice) {
        roomRepository.insertNewPrice(bitcoinPrice);
        System.out.println("Add latest bitcoin price to database successfully");
    }

    /**
     * This method delete all data in room database
     */
    public void deleteAll() {
        roomRepository.deleteAll();
    }

    /**
     * This method fetching data from server once
     */
    public void fetchDataOnce() {
        constraint.fetchDataOnce();
    }
}
