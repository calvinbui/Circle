/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */

package com.id11413010.circle.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.id11413010.circle.app.dao.User_DAO;
import com.id11413010.circle.app.pojo.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity allows the user to create a new account. The new account will permit them access
 * to the application's features once they have logged in. The activity receives user input and
 * passes this information to be stored into a database.
 */
public class Register extends Activity {
    /**
     * EditText representing a first name
     */
    private EditText firstName;
    /**
     * EditText representing a last name
     */
    private EditText lastName;
    /**
     * EditText representing an email
     */
    private EditText email;
    /**
     * EditText representing a password
     */
    private EditText password;
    /**
     * EditText representing a circle/group of friends
     */
    private EditText circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //finds and stores a view that was identified by the id attribute
        firstName = (EditText)findViewById(R.id.firstNameRegister_et); //EditText for first name
        lastName = (EditText)findViewById(R.id.lastNameRegister_et); //EditText for last name
        email = (EditText)findViewById(R.id.emailRegister_et); //EditText for email
        password = (EditText)findViewById(R.id.passwordRegister_et); //EditText for password
        circle = (EditText)findViewById(R.id.circleRegister_et); //EditText for circle/group
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    /**
     * A method called by a button widget within the layout XML. Will execute an AsyncTask if there
     * all fields are completed. The AsyncTask will register create a new row in the database.
     * @param v View object;
     */
    public void registerUser(View v) {
        // check if all fields are complete
        if (checkText()) {
            // executes the AsyncTask to create a new user.
            new RegisterTask().execute();
        }
    }

    /**
     * Checks for any incomplete EditText fields in the registration form. If there are, it will
     * present a toast to the user stating which field is missing.
     * @return boolean 'true' if all fields are complete, else 'false'.
     */
    private boolean checkText() {
        if (firstName.getText().toString().equals("")) {
            // create a toast with a message of what is missing.
            makeToast(getText(R.string.firstNameMissing).toString());
            return false;
        }
        else if (lastName.getText().toString().equals("")) {
            makeToast(getText(R.string.lastNameMissing).toString());
            return false;
        }
        else if (email.getText().toString().equals("")) {
            makeToast(getText(R.string.emailMissing).toString());
            return false;
        }
        else if (password.getText().toString().equals("")) {
            makeToast(getText(R.string.passwordMissing).toString());
            return false;
        }
        else if (circle.getText().toString().equals("")) {
            makeToast(getText(R.string.circleMissing).toString());
            return false;
        }
        return true;
    }

    /**
     * Create a new toast when given a String. The String becomes the toast message.
     * @param message String for the toast.
     */
    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread.
     */
    private class RegisterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            /*HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.DB_URL + "register.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair(Constants.DB_FIRSTNAME, ));
            nameValuePairs.add(new BasicNameValuePair(Constants.DB_LASTNAME, ));
            nameValuePairs.add(new BasicNameValuePair(Constants.DB_EMAIL, ));
            nameValuePairs.add(new BasicNameValuePair(Constants.DB_PASSWORD, ));
            nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle.getText().toString()));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            User user = new User(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    circle.getText().toString()
            );

            User_DAO.createUser(user);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            startActivity(new Intent(Register.this, Login.class));
        }
    }
}