package com.example.bitcoinmarketprice.room;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.bitcoinmarketprice.workmanager.NetworkConstraint;

import java.util.List;

public class RoomRepository {

    private static final String TAG = "RoomRepository";
    private final Context context;

    public RoomRepository(Context context) {
        this.context = context;
    }

    public MutableLiveData<List<BitcoinPrice>> getAllHistoricPrice() {
        CoinDatabase coinDatabase = Room
                .databaseBuilder(context, CoinDatabase.class, "bitcoin-price")
                .build();

        CoinDao coinDao = coinDatabase.coinDao();

        final MutableLiveData<List<BitcoinPrice>> bitcoinMetaMutableLiveData = new MutableLiveData<>();

        final List<BitcoinPrice>[] priceList = new List[]{null};
        Thread thread = new Thread(() -> priceList[0] = coinDao.getAll());
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        bitcoinMetaMutableLiveData.setValue(priceList[0]);

        return bitcoinMetaMutableLiveData;
    }

    public MutableLiveData<BitcoinPrice> getLatestItem() {
        CoinDatabase coinDatabase = Room
                .databaseBuilder(context, CoinDatabase.class, "bitcoin-price")
                .build();

        CoinDao coinDao = coinDatabase.coinDao();

        final MutableLiveData<BitcoinPrice> bitcoinMetaMutableLiveData = new MutableLiveData<>();
        new Thread(() -> bitcoinMetaMutableLiveData.postValue(coinDao.getLatestItem())).start();

        return bitcoinMetaMutableLiveData;
    }

    public void insertNewBitcoinPrice(BitcoinPrice bitcoinPrice) {
        CoinDatabase coinDatabase = Room
                .databaseBuilder(context, CoinDatabase.class, "bitcoin-price")
                .build();

        CoinDao coinDao = coinDatabase.coinDao();

        final BitcoinPrice[] priceList = {null};
        Thread thread = new Thread(() -> priceList[0] = coinDao.getLatestItem());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If the last updated item's request time is the same as the latest requested from server
        // then we don't update database table
        String latestLoadedItem = bitcoinPrice.getRequestTime();
        if (priceList[0] != null) {
            if (priceList[0].getRequestTime().equals(latestLoadedItem)) return;
        }

        new Thread(() -> coinDao.insertNewPrice(bitcoinPrice)).start();

        System.out.println("Add latest bitcoin price to database successfully");
    }

    public void deleteAll() {
        CoinDatabase coinDatabase = Room
                .databaseBuilder(context, CoinDatabase.class, "bitcoin-price")
                .build();

        CoinDao coinDao = coinDatabase.coinDao();

        new Thread(coinDao::deleteAll).start();
    }
}