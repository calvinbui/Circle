/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.content.Intent;
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
import com.id11413010.circle.app.dao.PollDAO;
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
     * List that will host our items and allow us to modify the VotingAdapter
     */
    private ArrayList<Poll> arrayList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        listView = (ListView)findViewById(R.id.pollList);
        // initialise arrayList
        arrayList = new ArrayList<Poll>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new VotingAdapter(this, R.layout.listpolls, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        new retrievePollsTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                Poll item = (Poll)adapterView.getItemAtPosition(position);
                Intent i = new Intent(Voting.this, VotingView.class);
                i.putExtra(Constants.POLL_ID, item.getId());
                i.putExtra(Constants.POLL_NAME, item.getName());
                startActivity(i);
            }
        });
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

    public class retrievePollsTask extends AsyncTask<Void, Void, Void> {
        private String json;

        protected Void doInBackground(Void... params) {
            json = PollDAO.retrieveAllPolls(Voting.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Type collectionType = new TypeToken<ArrayList<Poll>>(){}.getType();
            List<Poll> list = new Gson().fromJson(json, collectionType);
            for (Poll p : list)
                arrayList.add(p);
            adapter.notifyDataSetChanged();
        }
    }
}