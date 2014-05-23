/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.id11413010.circle.app.R;

/**
 The class is used for listing the current and future 'events' each group of friends has created.
 Events are retrieved from a database and shown within a List Activity.
 */
public class Events extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // if the action bar item is the add event menu button
        if (id == R.id.addEventMenu) {
            // start an activity to add a new event
            startActivity(new Intent(this, EventAdd.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
