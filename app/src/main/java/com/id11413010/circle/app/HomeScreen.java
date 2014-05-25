/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.id11413010.circle.app.events.Events;
import com.id11413010.circle.app.friends.Friends;
import com.id11413010.circle.app.money.MoneyHome;
import com.id11413010.circle.app.voting.Voting;

/**
 * This activity is a 'home screen' for navigation between the various activities and functions of
 * the application. It is presented to the user after a success 'Login'. It shows six buttons which
 * start a different type of activity for each.
 */
public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        // retrieve the user's first name from the application's shared preferences
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        // store the first name into a String variable
        String firstName = sp.getString(Constants.FIRSTNAME, getText(R.string.tester).toString());
        // present a toast welcoming the user upon logging in
        Toast.makeText(getApplicationContext(), getText(R.string.welcomeMsg).toString() + " " + firstName, Toast.LENGTH_LONG).show();
    }

    /**
     * A method called by the layout XML. Starts a new activity based upon which button  is press.
     * @param v View object
     */
    public void openFriends(View v) {
        // start the friend_list activity
        startActivity(new Intent(this, Friends.class));
    }

    public void openEvents(View v) {
        // start the event activity
        startActivity(new Intent(this, Events.class));
    }

    public void openMoney(View v) {
        // start the money activity
        startActivity(new Intent(this, MoneyHome.class));
    }

    public void openVoting(View v) {
        // start the money activity
        startActivity(new Intent(this, Voting.class));
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
