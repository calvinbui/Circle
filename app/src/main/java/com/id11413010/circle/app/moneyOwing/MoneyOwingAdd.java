package com.id11413010.circle.app.moneyOwing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

public class MoneyOwingAdd extends Activity {
    private Spinner spinner;
    private Integer userId;
    private List<Integer> userIdList;
    private EditText amount;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_owing);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner)findViewById(R.id.moneyOwer);
        amount = (EditText)findViewById(R.id.moneyAmountOwed);
        description = (EditText)findViewById(R.id.moneyOwedDescription);
        new retrieveUsersTask().execute();
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
            new createMoneyOwingTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createSpinner(List<User> names) {
        List<String> list = new ArrayList<String>();
        userIdList = new ArrayList<Integer>();
        for(User name : names) {
            list.add(name.getFirstName());
            userIdList.add(name.getId());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MoneyOwing.class));
        finish();
    }

    private class retrieveUsersTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            return UserDAO.retrieveAllUsers(MoneyOwingAdd.this);
        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> list = new Gson().fromJson(json, collectionType);
            createSpinner(list);
        }
    }

    private class createMoneyOwingTask extends AsyncTask<Void, Void, Void> {
        private Money money;

        protected void onPreExecute() {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            String circle = sp.getString(Constants.CIRCLE, null);
            int currentUser = sp.getInt(Constants.USERID, 0);
            userId = userIdList.get(spinner.getSelectedItemPosition());
            money = new Money(circle, currentUser, userId, Double.parseDouble(amount.getText().toString()),0,description.getText().toString(), null);
        }

        protected Void doInBackground(Void... params) {
            MoneyDAO.createOwing(money);
            return null;
        }

        protected void onPostExecute(Void result) {
            startActivity(new Intent(MoneyOwingAdd.this, MoneyOwing.class));
            finish();
        }
    }
}
