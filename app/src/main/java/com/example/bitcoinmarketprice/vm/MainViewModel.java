package com.example.bitcoinmarketprice.vm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.retrofit.RetrofitRepository;
import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.room.RoomRepository;
import com.example.bitcoinmarketprice.workmanager.NetworkConstraint;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    RetrofitRepository retrofitRepository;
    RoomRepository roomRepository;
    NetworkConstraint constraint;

    public MainViewModel(@NonNull Application application) {
        super(application);
        constraint = NetworkConstraint.getInstance(application);
        retrofitRepository = new RetrofitRepository();
        roomRepository = new RoomRepository(application);
    }

    /**
     * This method request bitcoin price meta data from server and return in java's POJO class
     */
    public MutableLiveData<BitcoinMeta> getBitcoinMetaData() {
        return retrofitRepository.getBitcoinMetaData();
    }

    /**
     * This method retrieve bitcoin historic price data from BitcoinMeta table and return in java's POJO class
     */
    public LiveData<List<BitcoinPrice>> getBitcoinHistoricPrice() {
        return roomRepository.getAllHistoricPrice();
    }

    /**
     * This method retrieve latest bitcoin data from table
     * */
    public MutableLiveData<BitcoinPrice> getLatestBitcoinPrice() {
        return roomRepository.getLatestItem();
    }

    /**
     * This method insert new updated bitcoin price to table
     * */
    public void insertNewBitcoinPrice (BitcoinPrice bitcoinPrice) {
        roomRepository.insertNewBitcoinPrice(bitcoinPrice);
    }

    /**
     * This method delete all data in room database
     * */
    public void deleteAll () {
        roomRepository.deleteAll();
    }

    /**
     * This method fetching data from server once
     * */
    public void fetchDataOnce() {
        constraint.fetchDataOnce();
    }
}
