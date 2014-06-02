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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.EventDAO;
import com.id11413010.circle.app.home.HomeScreen;
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
        // start an async task to retrieve all events within the user's circle
        new retrieveEventsTask().execute();

        // listen for a click on a particular row on the list and show a popup dialog of the event's description
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                // retrieve the Event object at the position clicked
                Event item = (Event)adapterView.getItemAtPosition(position);
                // create a new alert dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(Events.this);
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                // only pick one item from the list
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                // set the selected item as checked
                listView.setItemChecked(i, true);
                //final Money longItem = (Money)adapterView.getItemAtPosition(i);

                startActionMode(new ActionMode.Callback() {
                    // Called when the action mode is created; startActionMode() was called
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        // Inflate a menu resource providing context menu items
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.action_mode_delete, menu);
                        return true;
                    }

                    // Called each time the action mode is shown. Always called after onCreateActionMode, but
                    // may be called multiple times if the mode is invalidated.
                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false; // Return false if nothing is done
                    }

                    // Called when the user selects a contextual menu item
                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteListObject:
                                int pos = listView.getCheckedItemPosition();
                                Event e = (Event)listView.getItemAtPosition(pos);
                                // delete the event
                                new DeleteEventTask(e).execute();
                                Toast.makeText(getApplicationContext(), getString(R.string.eventDeleted), Toast.LENGTH_SHORT).show();
                                mode.finish(); // Action picked, so close the CAB
                                return true;
                            default:
                                return false;
                        }
                    }

                    // Called when the user exits the action mode
                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        listView.clearChoices();
                        //workaround for some items not being unchecked.
                        //see http://stackoverflow.com/a/10542628/1366471
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            (listView.getChildAt(i).getBackground()).setState(new int[] { 0 });
                        }
                        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    }
                });
                return true;
            }
        });

        Log.i(Constants.LOG, "Started Events");
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

    /**
     * Sort the list based on start date in ascending order
     */
    private void sort() {
        Collections.sort(arrayList, new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                int i = 0;
                try{
                    return new SimpleDateFormat("yyyy-MM-dd").parse(o1.getStartDate()).compareTo(new SimpleDateFormat("yyyy-MM-dd").parse(o2.getStartDate()));
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
    public class retrieveEventsTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            // retrieve circle id from shared preferences
            return EventDAO.retrieveEvents(Events.this);
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

    /**
     * TODO
     */
    private class DeleteEventTask extends AsyncTask<Void, Void, Void>{
        /**
         * A event object to delete
         */
        private Event event;

        private DeleteEventTask(Event event) {
            this.event = event;
        }

        protected Void doInBackground(Void... params) {
            // delete the money object
            EventDAO.deleteEvent(event);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // remove the object from the arraylist
            adapter.remove(event);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }
}