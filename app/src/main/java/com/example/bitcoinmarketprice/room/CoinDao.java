package com.example.bitcoinmarketprice.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bitcoinmarketprice.model.BitcoinMeta;

import java.util.List;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM bitcoinprice")
    List<BitcoinPrice> getAll();

    @Query("SELECT * FROM bitcoinprice ORDER BY request_time DESC LIMIT 1")
    BitcoinPrice getLatestItem();

    @Insert(entity = BitcoinPrice.class)
    void insertNewPrice(BitcoinPrice bitcoinPrice);

    @Insert
    void insetAll(BitcoinPrice... bitcoinPrice);

    @Delete
    void delete(BitcoinPrice bitcoinPrice);

    @Query("DELETE FROM bitcoinprice")
    void deleteAll();
}
