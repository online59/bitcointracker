package com.example.bitcoinmarketprice.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM bitcoinprice")
    LiveData<List<BitcoinPrice>> getAll();

    @Insert(entity = BitcoinPrice.class)
    void insertNewPrice(BitcoinPrice bitcoinPrice);

    @Query("DELETE FROM bitcoinprice")
    void deleteAll();
}
