package com.example.bitcoinmarketprice.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bitcoinmarketprice.model.BitcoinMeta;

@Database(entities = {BitcoinPrice.class}, version = 1)
public abstract class CoinDatabase extends RoomDatabase {
    public abstract CoinDao coinDao();
}
