package com.example.bitcoinmarketprice.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bitcoinmarketprice.model.BitcoinMeta;

@Database(entities = {BitcoinPrice.class}, version = 1)
public abstract class CoinDatabase extends RoomDatabase {
    public abstract CoinDao coinDao();

    private static CoinDatabase instance;

    // Make a method synchronized so other thread have to wait on this method
    public static synchronized CoinDatabase getInstance(Context context) {

        // Check whether the coin database instance is already created
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), CoinDatabase.class, "bitcoin-price")
                    .build();
        }

        return instance;
    }
}
