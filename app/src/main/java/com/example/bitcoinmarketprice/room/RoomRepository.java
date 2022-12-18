package com.example.bitcoinmarketprice.room;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        MutableLiveData<BitcoinPrice> bitcoinPriceMutableLiveData = new MutableLiveData<>();

        new Thread(() -> bitcoinPriceMutableLiveData.postValue(coinDao.getLatestItem())).start();

        return bitcoinPriceMutableLiveData;
    }

    public void insertNewBitcoinPrice(BitcoinPrice bitcoinPrice) {
        new Thread(() -> coinDao.insertNewPrice(bitcoinPrice)).start();
    }

    public void checkInsertNewBitcoinPrice(BitcoinPrice bitcoinPrice) {

        AtomicReference<String> lastTime = new AtomicReference<>();

        Thread thread = new Thread(() -> lastTime.set(coinDao.getLatestItem().getRequestTime()));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String newTime = bitcoinPrice.getRequestTime();

        if (!lastTime.toString().equals(newTime)) {
            new Thread(() -> coinDao.insertNewPrice(bitcoinPrice)).start();
        }

    }

    public void deleteAll() {
        new Thread(coinDao::deleteAll).start();
    }
}