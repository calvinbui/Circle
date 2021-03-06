/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */

package com.id11413010.circle.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.login.Login;
import com.id11413010.circle.app.pojo.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //finds and stores a view that was identified by the id attribute
        firstName = (EditText)findViewById(R.id.firstNameRegister); //EditText for first name
        lastName = (EditText)findViewById(R.id.lastNameRegister); //EditText for last name
        email = (EditText)findViewById(R.id.emailRegister); //EditText for email
        password = (EditText)findViewById(R.id.passwordRegister); //EditText for password
        circle = (EditText)findViewById(R.id.circleRegister); //EditText for circle/group
        Log.i(Constants.LOG, "Started Register Activity");
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
        else if(!isEmailValid(email.getText().toString())) {
            makeToast(getString(R.string.invalidEmail));
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
    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... params) {
            // create a new User object containing user data
            User user = new User(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),password.getText().toString(),circle.getText().toString(), null);
            // pass the object to the data-access-object class to add it to the database
            return UserDAO.createUser(user);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // check if the user has successfully registered
            if (result) {
                // start the login activity, returning the user to the previous screen after registration
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
            // if the user has failed to register
            else
            // toast to notify the error registrating
                Toast.makeText(getApplicationContext(), getString(R.string.errorRegistering), Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * method is used for checking valid email id format.
     *  http://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}