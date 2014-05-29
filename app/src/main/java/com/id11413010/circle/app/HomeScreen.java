/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.id11413010.circle.app.events.Events;
import com.id11413010.circle.app.friends.Friends;
import com.id11413010.circle.app.leaderboards.LeaderboardHome;
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
    }

    /**
     * A method called by the layout XML. Starts the Friends activity based upon which button is press.
     * @param v View object
     */
    public void openFriends(View v) {
        // start the friend_list activity
        startActivity(new Intent(this, Friends.class));
    }

    /**
     * A method called by the layout XML. Starts the Events activity based upon which button is press.
     * @param v View object
     */
    public void openEvents(View v) {
        // start the event activity
        startActivity(new Intent(this, Events.class));
    }

    /**
     * A method called by the layout XML. Starts the Money activity based upon which button is press.
     * @param v View object
     */
    public void openMoney(View v) {
        // start the money activity
        startActivity(new Intent(this, MoneyHome.class));
    }

    /**
     * A method called by the layout XML. Starts the Voting activity based upon which button is press.
     * @param v View object
     */
    public void openVoting(View v) {
        // start the money activity
        startActivity(new Intent(this, Voting.class));
    }

    /**
     * A method called by the layout XML. Starts the Leaderboard activity based upon which button is press.
     * @param v View object
     */
    public void openLeaderboard(View v) {
        startActivity(new Intent(this, LeaderboardHome.class));
    }

    /**
     * A method called by the layout XML. Starts the Friends activity based upon which button is press.
     */
    @Override
    public void onBackPressed()
    {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}