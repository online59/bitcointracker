package com.example.bitcoinmarketprice.util;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.bitcoinmarketprice.api.BroadcastService;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyUtils {

    public static final String MESSAGE = "FragmentMessage";
    public static final String INTENT_LAST_ITEM_IN_DATABASE = "com.example.bitcoinmarketprice.LastItemInDatabase";
    public static final String INTENT_PREVIOUS_REQUEST_TIME = "com.example.bitcoinmarketprice.PreviousRequestTime";

    // Convert date (from specific format) to new format
    public static String getItemDate(String itemDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss 'UTC'", Locale.US);
        Date date;
        try {
            date = inputFormat.parse(itemDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy KK:mm a", Locale.US);
        outputFormat.setTimeZone(TimeZone.getTimeZone("Thailand/Bangkok"));
        // Convert to Thailand time zone
        final long millisToAdd = 50_400_000;
        date.setTime(date.getTime() + millisToAdd);
        return outputFormat.format(date);
    }


    // Convert date (from specific format) into hour
    public static String getItemTime(String itemDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss 'UTC'", Locale.US);
        Date date;
        try {
            date = inputFormat.parse(itemDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mm a", Locale.US);
        outputFormat.setTimeZone(TimeZone.getTimeZone("Thailand/Bangkok"));
        // Convert to Thailand time zone
        final long millisToAdd = 50_400_000;
        date.setTime(date.getTime() + millisToAdd);
        return outputFormat.format(date);
    }


    // Converting double format string to int format string
    public static String getWholePrice(String itemPrice) {
        double price = Double.parseDouble(itemPrice.replace(",", ""));
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(price);
    }

    public static String getPercentageChange(String currentRate, String previousRate) {
        String change = "0.0";
        double currentPrice;
        double previousPrice;
        if (previousRate  != null) {
            currentPrice = Double.parseDouble(currentRate.replace(",", ""));
            previousPrice = Double.parseDouble(currentRate.replace(",", ""));

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);

            change = numberFormat.format(currentPrice/previousPrice);
        }

        return change + "%";
    }

}
