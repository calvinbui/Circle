/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */

package com.id11413010.circle.app.money;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
public class Money_Split_Bill extends Activity implements NumberPicker.OnValueChangeListener{
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
        setContentView(R.layout.activity_money__split__bill);

        // finds and stores a view that was identified by the id attribute
        peopleCount = (NumberPicker)findViewById(R.id.numOfPeople_np); // NumberPicker for people within circle
        totalPrice = (EditText)findViewById(R.id.totalPrice_et); // EditText for total bill price
        splitPrice = (TextView)findViewById(R.id.splitPrice_tv); //TextView for the bill split price

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
        // set a ValueChangeListener onto the NumberPicker to update the TextView upon value changes to the NumberPicker
        peopleCount.setOnValueChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.money__split__bill, menu);
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
     * Calculates the split bill amount per person and sets the value to the TextView.
     */
    private void calculate() {
        // Double values representing the total price and amount of people selected.
        Double total, people;
        // A decimal formatter to round the double to two decimal places.
        DecimalFormat df = new DecimalFormat("#.00");
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
     * thread.
     */
    private class getCircleMembersCount extends AsyncTask<Void, Void, Void> {
        private String s;

        @Override
        protected Void doInBackground(Void... params) {
            // retrieves the User's Circle ID stored within the Shared Preferences and store it
            // within the String circle.
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            String circle = sp.getString(Constants.CIRCLE, null);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.DB_URL + "circle_member_count.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                // store the response containing the amount of people within the circle.
                HttpEntity entity = response.getEntity();
                // store the amount of people in a String.
                s = EntityUtils.toString(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // convert the amount of people from a String to an Integer
            int count = Integer.valueOf(s);
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
}
