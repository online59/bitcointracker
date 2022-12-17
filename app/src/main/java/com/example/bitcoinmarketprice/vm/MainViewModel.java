package com.example.bitcoinmarketprice.vm;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bitcoinmarketprice.model.BitcoinMeta;
import com.example.bitcoinmarketprice.retrofit.RetrofitRepository;
import com.example.bitcoinmarketprice.room.BitcoinPrice;
import com.example.bitcoinmarketprice.room.RoomRepository;
import com.example.bitcoinmarketprice.workmanager.NetworkConstraint;

import java.util.List;

public class MainViewModel extends ViewModel {

    RetrofitRepository retrofitRepository;
    RoomRepository roomRepository;

    public MainViewModel() {

    }

    /**
     * This method request bitcoin price meta data from server and return in java's POJO class
     */
    public MutableLiveData<BitcoinMeta> getBitcoinMetaData(Context context, LifecycleOwner lifecycle) {
        if (retrofitRepository == null) {
            retrofitRepository = new RetrofitRepository();
        }

        return retrofitRepository.getBitcoinMetaData();
    }

    /**
     * This method retrieve bitcoin historic price data from BitcoinMeta table and return in java's POJO class
     */
    public MutableLiveData<List<BitcoinPrice>> getBitcoinHistoricPrice(Context context) {
        if (roomRepository == null) {
            roomRepository = new RoomRepository(context);
        }

        return roomRepository.getAllHistoricPrice();
    }

    /**
     * This method retrieve latest bitcoin data from table
     * */
    public MutableLiveData<BitcoinPrice> getLatestBitcoinPrice(Context context) {
        if (roomRepository == null) {
            roomRepository = new RoomRepository(context);
        }

        return roomRepository.getLatestItem();
    }

    /**
     * This method insert new updated bitcoin price to table
     * */
    public void insertNewBitcoinPrice (Context context, BitcoinPrice bitcoinPrice) {
        if (roomRepository == null) {
            roomRepository = new RoomRepository(context);
        }

        roomRepository.insertNewBitcoinPrice(bitcoinPrice);

    }

    /**
     * This method delete all data in room database
     * */
    public void deleteAll (Context context) {
        if (roomRepository == null) {
            roomRepository = new RoomRepository(context);
        }

        roomRepository.deleteAll();
    }

    /**
     * This method fetching data from server once
     * */
    public void fetchDataOnce(Context context) {
        NetworkConstraint.getInstance(context).fetchDataOnce();
    }
}
