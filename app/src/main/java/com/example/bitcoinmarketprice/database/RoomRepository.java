package com.example.bitcoinmarketprice.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class RoomRepository {

    private final CoinDao coinDao;
    private static RoomRepository instance;
    private static final String TAG = "RoomRepository";

    private RoomRepository(Application application) {
        CoinDatabase coinDatabase = CoinDatabase.getInstance(application);
        coinDao = coinDatabase.coinDao();
    }

    public static RoomRepository getInstance(Application application) {
        if (instance == null) {
            instance = new RoomRepository(application);
        }
        return instance;
    }

    public LiveData<List<BitcoinPrice>> getAllPrice() {
        return coinDao.getAll();
    }

    public LiveData<List<BitcoinPrice>> getLatestPrice() {
        return coinDao.getLatestItem();
    }

    public void insertNewPrice(BitcoinPrice bitcoinPrice) {
        Log.i(TAG, "insertNewPrice: New data has been added to the database.");
        new InsertItemAsync(coinDao).execute(bitcoinPrice);
    }

    public void deleteAll() {
        new DeleteItemAsync(coinDao).execute();
    }

    private static class InsertItemAsync extends AsyncTask<BitcoinPrice, Void, Void> {

        private final CoinDao coinDao;

        public InsertItemAsync(CoinDao coinDao) {
            this.coinDao = coinDao;
        }

        @Override
        protected Void doInBackground(BitcoinPrice... bitcoinPrices) {
            coinDao.insertNewPrice(bitcoinPrices[0]);
            return null;
        }
    }

    private static class DeleteItemAsync extends AsyncTask<Void, Void, Void> {

        private final CoinDao coinDao;

        public DeleteItemAsync(CoinDao coinDao) {
            this.coinDao = coinDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            coinDao.deleteAll();
            return null;
        }
    }
}