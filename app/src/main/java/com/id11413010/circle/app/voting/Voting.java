/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.voting;

import android.app.Activity;
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
import com.id11413010.circle.app.dao.PollDAO;
import com.id11413010.circle.app.home.HomeScreen;
import com.id11413010.circle.app.pojo.Poll;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is used for listing the Polls each group of friends has created.
 * Polls are retrieved from a database and shown within a List Activity.
 */
public class Voting extends Activity {
    /**
     * ListView that will hold our items references back to main.xml
     */
    private ListView listView;
    /**
     * Array Adapter that will hold our ArrayList and display the items on the ListView
     */
    private VotingAdapter adapter;
    /**
     * List that will host Poll items.
     */
    private ArrayList<Poll> arrayList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView)findViewById(R.id.pollList);
        // initialise arrayList
        arrayList = new ArrayList<Poll>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new VotingAdapter(this, R.layout.listpolls, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        // retrieve the polls for the circle from the database
        new RetrievePollsTask().execute();

        // listen for a click on a particular row on the list and opens the poll.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                // get the Poll object from the row selected
                Poll item = (Poll)adapterView.getItemAtPosition(position);
                // create a new intent
                Intent i = new Intent(Voting.this, VotingView.class);
                // put the poll id and name into the intent
                i.putExtra(Constants.POLL_ID, item.getId());
                i.putExtra(Constants.POLL_NAME, item.getName());
                // start the activity given the intent
                startActivity(i);
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
                                Poll p = (Poll)listView.getItemAtPosition(pos);
                                // delete the poll
                                new DeletePollTask(p).execute();
                                Toast.makeText(getApplicationContext(), getString(R.string.pollDeleted), Toast.LENGTH_SHORT).show();
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

        Log.i(Constants.LOG, "Started Voting");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.voting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addVotingMenu) {
            // start an activity to add a new Poll
            startActivity(new Intent(this, VotingAdd.class));
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
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for Polls from the database.
     */
    public class RetrievePollsTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            // return the polls from the database
            return PollDAO.retrieveAllPolls(Voting.this);
        }

        @Override
        protected void onPostExecute(String json) {
            // create a new list of poll objects from the json String
            Type collectionType = new TypeToken<ArrayList<Poll>>(){}.getType();
            // add each poll from the list into the array list
            List<Poll> list = new Gson().fromJson(json, collectionType);
            for (Poll p : list)
                arrayList.add(p);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * TODO
     */
    private class DeletePollTask extends AsyncTask<Void, Void, Void>{
        /**
         * A poll object to delete
         */
        private Poll poll;

        private DeletePollTask(Poll poll) {
            this.poll = poll;
        }

        protected Void doInBackground(Void... params) {
            // delete the money object
            PollDAO.deletePoll(poll);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // remove the object from the arraylist
            adapter.remove(poll);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }
}