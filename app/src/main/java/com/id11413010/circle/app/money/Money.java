/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.id11413010.circle.app.R;

/**
 The class is used for navigating between the two different money features of the application,
 to split a bill and; to create and view money owed to one another.
 */
public class Money extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.money, menu);
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
     * Linked to a Button onClick within the XML to open the Split Bill activity
     */
    public void openSplitBill(View v) {
        startActivity(new Intent(this, Money_Split_Bill.class));
    }

    /**
     * Linked to a Button onClick within the XML to open the money owing activity
     */
    public void openMoneyOwing(View v) {
        //startActivity(new Intent(this, Money_Owing.class));
    }
}
