/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */

package com.id11413010.circle.app.moneySplit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.UserDAO;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is to calculate the divide a payment within a specified amount of individuals (friends).
 *
 * The activity also retrieves the number of friends within a circle and sets it as the max value on the
 * NumberPicker.
 *
 * Changes to the total price of the bill or number picker are registered to automatically
 * update the 'per person' amount of the bill dynamically using a TextWatcher and ValueChange
 * listener.
 */
public class MoneySplit extends Activity implements NumberPicker.OnValueChangeListener{
    /**
     * A NumberPicker representing values between 1 and the amount of people within a circle. Used
     * to divide the total amount.
     */
    private NumberPicker peopleCount;
    /**
     * An EditText representing the total price of the bill to be split.
     */
    private EditText totalPrice;
    /**
     * A TextView which is displays the split amount of the bill.
     */
    private TextView splitPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_split_bill);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // finds and stores a view that was identified by the id attribute
        peopleCount = (NumberPicker)findViewById(R.id.numOfPeople); // NumberPicker for people within circle
        totalPrice = (EditText)findViewById(R.id.totalPrice); // EditText for total bill price
        splitPrice = (TextView)findViewById(R.id.splitPrice); //TextView for the bill split price

        // execute an AsyncTask which will retrieve the amount of people within the circle and configure the NumberPicker
        new getCircleMembersCount().execute();

        // a Text Watcher which updates the per person price TextView upon changes to the Total Price EditText
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // runs this method to update the TextView upon changes to the EditText
                calculate();
            }
        };
        // set the TextWatcher listener onto the total price EditText
        totalPrice.addTextChangedListener(textWatcher);
        totalPrice.setFilters(new InputFilter[]{new InputFilterCurrency(2)});
        // set a ValueChangeListener onto the NumberPicker to update the TextView upon value changes to the NumberPicker
        peopleCount.setOnValueChangedListener(this);
        Log.i(Constants.LOG, "Started Money Split");
    }

    /**
     * Calculates the split bill amount per person and sets the value to the TextView.
     */
    private void calculate() {
        // Double values representing the total price and amount of people selected.
        Double total, people;
        // A decimal formatter to round the double to two decimal places.
        DecimalFormat df = new DecimalFormat("#0.00");
        // Retrieve and store the amount of people selected on the NumberPicker
        people = (double) peopleCount.getValue();
        // An if statement obtain the total price entered.
        if(totalPrice.getText().toString().matches("")) { // if the EditText is blank
            // sets the total price to $0.00
            total = 0.0;
        }
        else {
            // retrieve and store price entered in the Total Price EditText and convert it into a double
            total = Double.valueOf(totalPrice.getText().toString());
        }
        // set the TextView to the split price by dividing the total price by the amount of people.
        splitPrice.setText(getText(R.string.dollarSign).toString() + df.format(total / people));
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i2) {
        // runs this method to update the TextView upon changes to the value selected in the NumberPicker
        calculate();
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Retrieves the amount of members within the circle.
     */
    private class getCircleMembersCount extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // retrieves the circle ID from Shared Preferences
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            String circle = sp.getString(Constants.CIRCLE, null);
            // retrieves the User's Circle ID stored within the Shared Preferences and store it
            // within the String circle.
            return UserDAO.retrieveCircleMemberCount(circle);
        }

        @Override
        protected void onPostExecute(String amount) {
            // convert the amount of people from a String to an Integer
            int count = Integer.valueOf(amount);
            // set the minimum amount of the NumberPicker to 1 as there will always be at least one person paying the bill
            peopleCount.setMinValue(1);
            // sets the max amount of the NumberPicker to the number of people in the circle
            peopleCount.setMaxValue(count);
            // TO-DO
            peopleCount.setWrapSelectorWheel(false);
            // set the default value of the NumberPicker to 1
            peopleCount.setValue(1);
        }
    }

    /**
     * InputFilter to limit currency amount to two decimal places.
     * http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
     */
    private class InputFilterCurrency implements InputFilter {
        private Pattern moPattern;

        public InputFilterCurrency(int aiMinorUnits) {
            // http://www.regexplanet.com/advanced/java/index.html
            moPattern = Pattern.compile("[0-9]*+((\\.[0-9]{0," + aiMinorUnits + "})?)||(\\.)?");
        } // InputFilterCurrency

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String lsStart = "";
            String lsInsert = "";
            String lsEnd = "";
            String lsText = "";

            Log.d("debug", moPattern.toString());
            Log.d("debug", "source: " + source + ", start: " + start + ", end:" + end + ", dest: " + dest + ", dstart: " + dstart + ", dend: " + dend);

            lsText = dest.toString();

            // If the length is greater then 0, then insert the new character
            // into the original text for validation
            if (lsText.length() > 0) {
                lsStart = lsText.substring(0, dstart);
                Log.d("debug", "lsStart : " + lsStart);
                // Check to see if they have deleted a character
                if (source != "") {
                    lsInsert = source.toString();
                    Log.d("debug", "lsInsert: " + lsInsert);
                } // if
                lsEnd = lsText.substring(dend);
                Log.d("debug", "lsEnd   : " + lsEnd);
                lsText = lsStart + lsInsert + lsEnd;
                Log.d("debug", "lsText  : " + lsText);
            } // if

            Matcher loMatcher = moPattern.matcher(lsText);
            Log.d("debug", "loMatcher.matches(): " + loMatcher.matches() + ", lsText: " + lsText);
            if (!loMatcher.matches()) {
                return "";
            }
            return null;
        } // CharSequence
    } // InputFilterCurrency
}
