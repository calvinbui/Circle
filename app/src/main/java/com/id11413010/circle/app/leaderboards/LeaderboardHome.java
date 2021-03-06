package com.id11413010.circle.app.leaderboards;

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
import com.id11413010.circle.app.dao.LeaderboardDAO;
import com.id11413010.circle.app.home.HomeScreen;
import com.id11413010.circle.app.pojo.Leaderboard;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for listing leaderboards a group of friends has created.
 * Retrieves leaderboards from the database and displays them in a list view.
 */
public class LeaderboardHome extends Activity {
    /**
     * ListView that will hold our items references back to main.xml
     */
    private ListView listView;
    /**
     * Array Adapter that will hold our ArrayList and display the items on the ListView
     */
    private LeaderboardAdapter adapter;
    /**
     * List that will host Leaderboard items
     */
    private ArrayList<Leaderboard> arrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_home);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // initialise the list
        listView = (ListView)findViewById(R.id.leaderboardList);
        // initialise arrayList
        arrayList = new ArrayList<Leaderboard>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new LeaderboardAdapter(this, R.layout.listleaderboardsall, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        // start an aysnc task to retrieve all leaderboards within the user's group
        new RetrieveLeaderboardTask().execute();

        // listen for a click on a particular row on the list opens a view of that particular leaderboard.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                // retrieve the Leaderboard object at the position clicked
                Leaderboard item = (Leaderboard)adapterView.getItemAtPosition(position);
                // create a new intent containing the leaderboard id and name
                Intent i = new Intent(LeaderboardHome.this, LeaderboardView.class);
                i.putExtra(Constants.LEADERBOARD_ID, item.getId());
                i.putExtra(Constants.LEADERBOARD_NAME, item.getName());
                // start the activity with the extras
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
                                Leaderboard l = (Leaderboard)listView.getItemAtPosition(pos);
                                // delete the poll
                                new DeleteLeaderboardTask(l).execute();
                                Toast.makeText(getApplicationContext(), getString(R.string.leaderboardDeleted), Toast.LENGTH_SHORT).show();
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

        Log.i(Constants.LOG, "Started Leaderboard");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leaderboard_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addLeaderboard) {
            // start an activity to add a new leaderboard
            startActivity(new Intent(this, LeaderboardAdd.class));
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
     * thread. Responsible for retrieving leaderboards from the database.
     */
    private class RetrieveLeaderboardTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            // retrieve all leaderboards in the database within the circle of friends
            return LeaderboardDAO.retrieveAllLeaderboards(LeaderboardHome.this);

        }

        @Override
        protected void onPostExecute(String json) {
            // create a new list of leaderboard objects from the json String
            Type collectionType = new TypeToken<ArrayList<Leaderboard>>(){}.getType();
            List<Leaderboard> list = new Gson().fromJson(json, collectionType);
            // add each leaderboard from the list into the array list
            for (Leaderboard l : list)
                arrayList.add(l);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for deleting a leaderboard from the database.
     */
    private class DeleteLeaderboardTask extends AsyncTask<Void, Void, Void>{
        /**
         * A leaderboard object to delete
         */
        private Leaderboard leaderboard;

        private DeleteLeaderboardTask(Leaderboard leaderboard) {
            this.leaderboard = leaderboard;
        }

        protected Void doInBackground(Void... params) {
            // delete the money object
            LeaderboardDAO.deleteLeaderboard(leaderboard);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // remove the object from the arraylist
            adapter.remove(leaderboard);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }
}
