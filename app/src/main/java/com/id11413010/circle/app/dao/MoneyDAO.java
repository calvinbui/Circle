package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.network.Network;
import com.id11413010.circle.app.pojo.Money;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A data access object class which interacts with a web service to create, retrieve, update
 * and delete Money objects from the database.
 */
public class MoneyDAO {
    /**
     * Creates a new money owing row into the database by passing through user and money information
     * @param money A money object containing all information about hte oustanding payment
     */
    public static void createOwing(Money money) {
        // creates a list array which will contain information about the outstanding payment
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, money.getCircle())); //circle id
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LENDER, Integer.toString(money.getFrom()))); // the lender user id
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_PAYER, Integer.toString(money.getTo()))); //the ower user is
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_AMOUNT, String.valueOf(money.getAmount()))); //amount owed
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_DESCRIPTION, money.getDescription())); //description of the payment
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        Network.httpConnection("create_money_owing.php", nameValuePairs);
    }

    /**
     * Retrieve all outstanding payments within the user's group from the database
     * @param context The application's context
     * @return A JSON String containing all outstanding payments witihn the database
     */
    public static String retrieveOwing(Context context) {
        // retrieve the user's circle id from the local shared preferences
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        // creates a list array which will contain information about the user's circle
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle)); // circle
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Network.httpConnection("get_money_owing.php", nameValuePairs);
    }

    /**
     * Retrieve all outstanding payments within the user's group from the database
     * @param context The application's context
     * @return A JSON String containing all past payments within the database
     */
    public static String retrievePaid(Context context) {
        // retrieve the user's circle id from the local shared preferences
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        // creates a list array which will contain information about the user's circle
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle)); // circle
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Network.httpConnection("get_money_owing_paid.php", nameValuePairs);
    }

    /**
     * Update an outstanding payment within the database to paid. Updates the paid column of the row.
     * @param money The outstanding payment
     */
    public static void updateOwing(Money money) {
        // creates a list array which will contain information about the outstanding payment
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.MONEY_ID, Integer.toString(money.getId()))); // payment id
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        Network.httpConnection("update_money_owing.php", nameValuePairs);
    }
}
