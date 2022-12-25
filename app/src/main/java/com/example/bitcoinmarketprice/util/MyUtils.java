package com.example.bitcoinmarketprice.util;

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
        double price = Double.parseDouble(itemPrice);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(price);
    }

}
