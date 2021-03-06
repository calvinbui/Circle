/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.events.Events;
import com.id11413010.circle.app.friends.Friends;
import com.id11413010.circle.app.leaderboards.LeaderboardHome;
import com.id11413010.circle.app.login.Login;
import com.id11413010.circle.app.moneyOwing.MoneyOwing;
import com.id11413010.circle.app.moneySplit.MoneySplit;
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
        Log.i(Constants.LOG, "Started Homescreen");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logoutBtn) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
            // set the title
            builder.setTitle(R.string.logout)
                    // set the message of the dialog
                    .setMessage(getString(R.string.logoutPrompt))
                    // set a button to cancel the dialog
                    .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })// set a button to logout
                    .setPositiveButton(getString(R.string.logoutConfirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i(Constants.LOG, "Log out");
                            // start logout the user
                            startActivity(new Intent(HomeScreen.this, Login.class));
                            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                            // clear the shared preferences
                            sp.edit().clear().commit();
                            finish();
                        }
                    });
            // show the alert dialog
            builder.show();
        }
        return super.onOptionsItemSelected(item);
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
     * A method called by the layout XML. Starts the Money owing activity based upon which button is press.
     * @param v View object
     */
    public void openMoneyOwing(View v) {
        // start the money owing activity
        startActivity(new Intent(this, MoneyOwing.class));
    }

    /**
     * A method called by the layout XML. Starts the Money activity based upon which button is press.
     * @param v View object
     */
    public void openMoneySplit(View v) {
        // start the money owing activity
        startActivity(new Intent(this, MoneySplit.class));
    }

    /**
     * A method called by the layout XML. Starts the Voting activity based upon which button is press.
     * @param v View object
     */
    public void openVoting(View v) {
        // start the voting activity
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