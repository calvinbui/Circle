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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private EditText username_et;
    /**
     * An EditText representing a password
     */
    private EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_et = (EditText)findViewById(R.id.username_et);
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
        String htmlResponse;

        protected Void doInBackground(Void... params) {
            user = UserDAO.retrieveUser(username_et.getText().toString(), password_et.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /*// create a new array from the database response, broken up into individual objects by
            // a colon.
            String[] parts = htmlResponse.split(":");
            // If the login is successful, store session information into Shared Preferences.
            if (parts[0].equals("loginCorrect")) {
                // create a new shared preference object
                SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                // edit shared preferences
                SharedPreferences.Editor editor = sp.edit();
                // insert userid, first name, last name and circle into shared preferences
                editor.putString(Constants.USERID, parts[1]);
                editor.putString(Constants.FIRSTNAME, parts[2]);
                editor.putString(Constants.LASTNAME, parts[3]);
                editor.putString(Constants.CIRCLE, parts[4]);
                // save the preferences
                editor.commit();
                // login in the user, redirect them to the home page.
                startActivity(new Intent(Login.this, HomeScreen.class));
            }
            else {
                // if login unsuccessful, toast the user with an error message.
                Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_SHORT).show();
            }*/
            if(user.getEmail().equals(username_et.getText().toString()) && user.getPassword().equals(password_et.getText().toString())) {
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
            } else {
            // if login unsuccessful, toast the user with an error message.
            Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
