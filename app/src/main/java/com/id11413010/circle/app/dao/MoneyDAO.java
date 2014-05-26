package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
import com.id11413010.circle.app.pojo.Money;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class MoneyDAO {
    public static void createOwing(Money money) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, money.getCircle()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LENDER, Integer.toString(money.getFrom())));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_PAYER, Integer.toString(money.getTo())));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_AMOUNT, String.valueOf(money.getAmount())));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_DESCRIPTION, money.getDescription()));
        // start a network task with the page to access and information (array list) to process.
        Network.httpConnection("create_money_owing.php", nameValuePairs);
    }

    public static String retrieveOwing(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
        return Network.httpConnection("get_money_owing.php", nameValuePairs);
    }

    public static void deleteOwing(Money money) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.MONEY_ID, Integer.toString(money.getId())));
        Network.httpConnection("delete_money_owing.php", nameValuePairs);
    }
}
