package com.example.bitcoinmarketprice.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM bitcoinprice")
    LiveData<List<BitcoinPrice>> getAll();

    @Query("SELECT * FROM bitcoinprice ORDER BY request_time DESC LIMIT 1")
    LiveData<BitcoinPrice> getLatestItem();

    @Insert(entity = BitcoinPrice.class, onConflict = OnConflictStrategy.IGNORE)
    void insertNewPrice(BitcoinPrice bitcoinPrice);

    @Query("DELETE FROM bitcoinprice")
    void deleteAll();
}
