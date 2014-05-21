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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.id11413010.circle.app.dao.UserDAO;
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
    private EditText email_et;
    /**
     * An EditText representing a password
     */
    private EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_et = (EditText)findViewById(R.id.username_et);
        password_et = (EditText)findViewById(R.id.password_et);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void loginUser(View v) {
        new LoginTask().execute();
    }

    public void openRegisterPage(View v) {
        startActivity(new Intent(this, Register.class));
    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {
        User user;
        protected Void doInBackground(Void... params) {
            // create a new user object based on username and password entered.
            user = UserDAO.retrieveUser(email_et.getText().toString(), password_et.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // if login unsuccessful, toast the user with an error message.
            if (user == null) {
                Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_SHORT).show();
            }
            // If the login is successful, store session information into Shared Preferences.
            else if(user.getEmail().equals(email_et.getText().toString()) && user.getPassword().equals(password_et.getText().toString())) {
                SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                // edit shared preferences
                SharedPreferences.Editor editor = sp.edit();
                // insert userid, first name, last name and circle into shared preferences
                editor.putString(Constants.FIRSTNAME, user.getFirstName());
                editor.putString(Constants.LASTNAME, user.getLastName());
                editor.putString(Constants.CIRCLE, user.getCircle());
                // save the preferences
                editor.commit();
                startActivity(new Intent(Login.this, HomeScreen.class));
            }
        }
    }
}
