/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
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
 The class is used for listing the past 'events' each group of friends has created.
 Events are retrieved from a database and shown within a List Activity.
 */
public class EventPast extends Activity {
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
        // start an async task to retrieve all events within the user's circle
        new retrievePastEventsTask().execute();

        // listen for a click on a particular row on the list and show a popup dialog of the event's description
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                // retrieve the Event object at the position clicked
                Event item = (Event)adapterView.getItemAtPosition(position);
                // create a new alert dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(EventPast.this);
                builder.setTitle(item.getName()) //set the title to the event name
                        .setMessage(item.getDescription()) //set the description to the event
                                // create a button to close the dialog
                        .setNeutralButton(R.string.oK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // show the dialog
                builder.show();
            }
        });

        Log.i(Constants.LOG, "Started Past Events");
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, Events.class));
        finish();
    }

    /**
     * Sort the list based on start date in ascending order
     */
    private void sort() {
        Collections.sort(arrayList, new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                int i = 0;
                try {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(o2.getStartDate()).compareTo(new SimpleDateFormat("yyyy-MM-dd").parse(o1.getStartDate()));
                } catch (ParseException e) {
                    Log.e(Constants.LOG, "Event Add - SimpleDateFormat ParseException");
                }
                return i;
            }
        });
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving Events from the database.
     */
    public class retrievePastEventsTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            // retrieve circle id from shared preferences
            return EventDAO.retrievePastEvents(EventPast.this);
        }

        @Override
        protected void onPostExecute(String json) {
            // create a new list of event objects from the json String
            Type collectionType = new TypeToken<ArrayList<Event>>(){}.getType();
            List<Event> list = new Gson().fromJson(json, collectionType);
            // add each event from the list into the array list
            for (Event e : list)
                arrayList.add(e);
            // sort the list based on start date
            sort();
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }
}
