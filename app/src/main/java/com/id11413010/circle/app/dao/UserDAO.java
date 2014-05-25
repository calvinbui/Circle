/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
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
 * A data access object class which interacts with a web service to create, retrieve, update and delete
 * objects from the User class.
 */
public class UserDAO {

    /**
     * Creates a new user into the database by passing through User information.
     * @param user A User object which holds information
     */
    public static void createUser(User user) {
        // creates a list array which will contain information about the User
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_FIRSTNAME, user.getFirstName())); // first name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LASTNAME, user.getLastName())); // last name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_EMAIL, user.getEmail())); // email
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_PASSWORD, user.getPassword())); //password
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, user.getCircle())); //circle
        // start a network task with the page to access and information (array list) to process.
        Network.httpConnection("register.php", nameValuePairs);
    }

    /**
     * Retrieve a user based on username and password entered.
     * @param username Username entered by the user .
     * @param password Password entered by the user.
     * @return A User object containing information about the user with corresponding username and password.
     */
    public static User retrieveUser(String username, String password) {
        // create a new Gson object to deserialize JSON code from the web service
        Gson gson = new Gson();
        // create a list array containing information to send to the web service
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_EMAIL, username)); // username
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_PASSWORD, password)); // password
        // store the JSON response from the web service into a String
        String response = Network.httpConnection("login.php", nameValuePairs);
        // if the JSON response is empty return null
        if (response.equals("")) {
            return null;
        } else {
            // else create a new User from the JSON response using Gson.
            return gson.fromJson(response, User.class);
        }
    }

    /**
     * Retrieves the number of users within the same circle as the current user.
     * @return A String containing the amount of users within the same circle
     */
    public static String retrieveCircleMemberCount(String circle) {
        // create a list array containing the circle ID to send to the web service
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
        // return the String response from the web service containing the amount of users.
        return Network.httpConnection("circle_member_count.php", nameValuePairs);
    }

    public static String retrieveAllUsers(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
        return Network.httpConnection("get_all_users.php", nameValuePairs);
    }
}