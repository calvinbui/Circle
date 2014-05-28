/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.HomeScreen;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.EventDAO;
import com.id11413010.circle.app.pojo.Event;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 The class is used for listing the current and future 'events' each group of friends has created.
 Events are retrieved from a database and shown within a List Activity.
 */
public class Events extends Activity {
    /**
     * ListView that will hold our items references back to main.xml
     */
    private ListView listView;
    /**
     * Array Adapter that will hold our ArrayList and display the items on the ListView
     */
    private EventAdapter adapter;

    /**
     * List that will host our items and allow us to modify the EventAdapter
     */
    private ArrayList<Event> arrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // initialise list view
        listView = (ListView)findViewById(R.id.eventList);
        // initialise arrayList
        arrayList = new ArrayList<Event>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new EventAdapter(this, R.layout.listevents, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        new retrieveEventsTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                Event item = (Event)adapterView.getItemAtPosition(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(Events.this);
                builder.setTitle(item.getName())
                .setMessage(item.getDescription())
                .setNeutralButton(R.string.oK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // Set the Icon for the Dialog
                builder.show();
            }
        });
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }

    public class retrieveEventsTask extends AsyncTask<Void, Void, Void> {
        String json;

        protected Void doInBackground(Void... params) {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            json = EventDAO.retrieveEvents(sp.getString(Constants.CIRCLE, null));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Type collectionType = new TypeToken<ArrayList<Event>>(){}.getType();
            List<Event> list = new Gson().fromJson(json, collectionType);
            for (Event e : list)
                arrayList.add(e);
            sort();
            adapter.notifyDataSetChanged();
        }

        private void sort() {
            Collections.sort(arrayList, new Comparator<Event>() {
                public int compare(Event o1, Event o2) {
                    int i = 0;
                    try{
                        return new SimpleDateFormat("yyyy-MM-dd").parse(o1.getStartDate()).compareTo(new SimpleDateFormat("yyyy-MM-dd").parse(o2.getStartDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return i;
                }
            });
        }
    }
}