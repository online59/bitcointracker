package com.example.bitcoinmarketprice.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

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

    public LiveData<BitcoinPrice> getLatestPrice() {
        return coinDao.getLatestItem();
    }

    public void insertNewPrice(BitcoinPrice bitcoinPrice) {
        Log.i(TAG, "insertNewPrice: New data has been added to the database.");
        new InsertItemAsync(coinDao).execute(bitcoinPrice);
    }

    public void checkInsertNewBitcoinPrice(BitcoinPrice updatePrice, BitcoinPrice previousPrice) {
        new InsertCheckItemAsync(coinDao, previousPrice).execute(updatePrice);
    }

    public void deleteAll() {
        new DeleteItemAsync(coinDao).execute();
    }

    private static class InsertCheckItemAsync extends AsyncTask<BitcoinPrice, Void, Void> {
        private final CoinDao coinDao;
        private final BitcoinPrice bitcoinPrice;

        public InsertCheckItemAsync(CoinDao coinDao, BitcoinPrice bitcoinPrice) {
            this.coinDao = coinDao;
            this.bitcoinPrice = bitcoinPrice;
        }

        @Override
        protected Void doInBackground(BitcoinPrice... bitcoinPrices) {
            String previousRequestTime = bitcoinPrice.getRequestTime();
            String latestRequestTime = bitcoinPrices[0].getRequestTime();

            // Check if the price has been added to the database
            if (!previousRequestTime.equalsIgnoreCase(latestRequestTime)) {
                // If not, then add new price to database
                coinDao.insertNewPrice(bitcoinPrices[0]);
            }
            return null;
        }
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