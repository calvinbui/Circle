/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.id11413010.circle.app.HomeScreen;
import com.id11413010.circle.app.R;

/**
 The class is used for navigating between the two different money features of the application,
 to split a bill and; to create and view money owed to one another.
 */
public class MoneyHome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Linked to a Button onClick within the XML to open the Split Bill activity
     */
    public void openSplitBill(View v) {
        startActivity(new Intent(this, MoneySplit.class));
    }

    /**
     * Linked to a Button onClick within the XML to open the money owing activity
     */
    public void openMoneyOwing(View v) {
        startActivity(new Intent(this, MoneyOwing.class));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }
}
