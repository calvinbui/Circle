/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.network.NetworkCheck;
import com.id11413010.circle.app.pojo.User;

/**
 * Allows individuals to logon as a user and access features of the application as a registered
 * member part of a circle/group of friends. Users enter a username and password which is authenticated
 * with the database. If never before registered, activity also provides a button to register as
 * a new member.
 */
public class Login extends Activity {
    /**
     * An EditText representing a username
     */
    private EditText emailET;
    /**
     * An EditText representing a password
     */
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //finds and stores a view that was identified by the id attribute
        emailET = (EditText)findViewById(R.id.username_et); //email EditText
        passwordET = (EditText)findViewById(R.id.password_et); //password EditText
        Log.i(Constants.LOG, "Started Application");
        startService(new Intent(this, NetworkCheck.class));
        Log.i(Constants.LOG, "Starting Network Check Service");
    }

    /**
     * Execute an AsyncTask to check credentials and login the user
     */
    public void loginUser(View v) {
        new LoginTask().execute();
    }

    /**
     * Creates an intent and opens the Register activity using that intent.
     */
    public void openRegisterPage(View v) {
        startActivity(new Intent(this, Register.class));
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for authenticating username and password and granting access. Also caches
     * information locally using Shared Preferences.
     */
    private class LoginTask extends AsyncTask<Void, Void, Void> {
        /**
         * A User object representing a user of the system
         */
        private User user;

        protected Void doInBackground(Void... params) {
            // create a new user object based on username and password entered.
            user = UserDAO.retrieveUser(emailET.getText().toString(), passwordET.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // if login unsuccessful, toast the user with an error message.
            if (user == null)
                Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_SHORT).show();
            // If the login is successful, store session information into Shared Preferences.
            else if(user.getEmail().equals(emailET.getText().toString()) && user.getPassword().equals(passwordET.getText().toString())) {
                SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                // edit shared preferences
                SharedPreferences.Editor editor = sp.edit();
                // insert userid, first name, last name and circle into shared preferences
                editor.putString(Constants.FIRSTNAME, user.getFirstName());
                editor.putString(Constants.LASTNAME, user.getLastName());
                editor.putString(Constants.CIRCLE, user.getCircle());
                editor.putInt(Constants.USERID, user.getId());
                // save the preferences
                editor.commit();
                // start the home screen activity
                startActivity(new Intent(Login.this, HomeScreen.class));
                finish();

                Toast.makeText(getApplicationContext(), getText(R.string.welcomeMsg).toString() + " " + user.getFirstName(), Toast.LENGTH_LONG).show();

            }
        }
    }
}
