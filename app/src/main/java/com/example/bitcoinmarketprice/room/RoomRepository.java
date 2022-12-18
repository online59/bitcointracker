package com.example.bitcoinmarketprice.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RoomRepository {

    private final CoinDao coinDao;

    public RoomRepository(Application application) {
        CoinDatabase coinDatabase = CoinDatabase.getInstance(application);
        coinDao = coinDatabase.coinDao();
    }

    public LiveData<List<BitcoinPrice>> getAllHistoricPrice() {
        return coinDao.getAll();
    }

    public LiveData<BitcoinPrice> getLatestItem() {
        return coinDao.getLatestItem();
    }

    public void insertNewBitcoinPrice(BitcoinPrice bitcoinPrice) {

        new Thread(() -> coinDao.insertNewPrice(bitcoinPrice)).start();
    }

    public void deleteAll() {
        new Thread(coinDao::deleteAll).start();
    }
}