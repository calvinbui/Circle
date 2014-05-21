package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
import com.id11413010.circle.app.pojo.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class UserDAO {

    public static void createUser(User user) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_FIRSTNAME, user.getFirstName()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LASTNAME, user.getLastName()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_EMAIL, user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_PASSWORD, user.getPassword()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, user.getCircle()));
        Network.httpConnection("register.php", nameValuePairs);
    }

    public static User retrieveUser(String username, String password) {
        Gson gson = new Gson();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_EMAIL, username));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_PASSWORD, password));
        String response = Network.httpConnection("login.php", nameValuePairs);
        if (response.equals("")) {
            return null;
        } else {
            return gson.fromJson(response, User.class);
        }
    }

    public static String retrieveCircleMemberCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
        return Network.httpConnection("circle_member_count.php", nameValuePairs);
    }
}
