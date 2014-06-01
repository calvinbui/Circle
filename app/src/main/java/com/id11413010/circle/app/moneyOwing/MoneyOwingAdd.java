package com.id11413010.circle.app.moneyOwing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.MoneyDAO;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.Money;
import com.id11413010.circle.app.pojo.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allow the user to add a new outstanding payment.
 */
public class MoneyOwingAdd extends Activity {
    /**
     * A Spinner which will hold users within the circle
     */
    private Spinner spinner;
    /**
     * List that will provide the user id for a user
     */
    private List<Integer> userIdList;
    /**
     * An EditText representing the payment amount
     */
    private EditText amount;
    /**
     * An EditText representing the payment description
     */
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_owing);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //finds and stores a view that was identified by the id attribute
        spinner = (Spinner) findViewById(R.id.moneyOwer);
        amount = (EditText) findViewById(R.id.moneyAmountOwed);
        // limit edit text to two decimal places
        amount.setFilters(new InputFilter[]{new InputFilterCurrency(2)});
        description = (EditText) findViewById(R.id.moneyOwedDescription);
        // retrieve users in the circle from the database
        new RetrieveUsersTask().execute();
        Log.i(Constants.LOG, "Started Money Owing Add");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.money_owing_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.createOwing) {
            // if all fields are filled in
            if (validate()) {
                // create the new outstanding payment
                new CreateMoneyOwingTask().execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MoneyOwing.class));
        finish();
    }

    /**
     * Ensure all fields are filled in.
     */
    private boolean validate() {
        if (amount.getText().toString().equals("") || description.getText().toString().equals("")) {
            // toast the user that all fields are required.
            Toast.makeText(getApplicationContext(), getText(R.string.missingFields).toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving Friends from the database.
     */
    private class RetrieveUsersTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            // retrieve all friends from the database
            return UserDAO.retrieveAllUsers(MoneyOwingAdd.this);
        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<User>>() {
            }.getType();
            List<User> list = new Gson().fromJson(json, collectionType);
            // initialise a String list for the spinner and a user id to specify the selected user.
            List<String> namesList = new ArrayList<String>();
            userIdList = new ArrayList<Integer>();
            // add each friend into the array list
            for (User name : list) {
                namesList.add(name.getFirstName());
                userIdList.add(name.getId());
            }
            // create an adapter to connect to a spinner containing a list of names
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MoneyOwingAdd.this, android.R.layout.simple_spinner_dropdown_item, namesList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // create a new spinner given a list of names
            spinner.setAdapter(arrayAdapter);
        }
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for creating a new outstanding payment.
     */
    private class CreateMoneyOwingTask extends AsyncTask<Void, Void, Void> {
        /**
         * A Money object containing information about the new outstanding payment.
         */
        private Money money;

        protected void onPreExecute() {
            // get the circle id and current user id from shared preferences
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            /*
            create a new money object with the obtained information, retrieve the chosen user from the spinner,
            user id and circle from shared preferences
             */
            money = new Money(sp.getString(Constants.CIRCLE, null), sp.getInt(Constants.USERID, 0), userIdList.get(spinner.getSelectedItemPosition()), Double.parseDouble(amount.getText().toString()), 0, description.getText().toString(), null);
        }

        protected Void doInBackground(Void... params) {
            // pass the object to a service to create a new payment in the database
            MoneyDAO.createOwing(money);
            return null;
        }

        protected void onPostExecute(Void result) {
            // return the user to the money owing home page
            startActivity(new Intent(MoneyOwingAdd.this, MoneyOwing.class));
            finish();
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
