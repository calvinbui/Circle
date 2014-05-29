package com.id11413010.circle.app.leaderboards;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.LeaderboardDAO;
import com.id11413010.circle.app.pojo.Ranking;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is used for listing the rankings within a leaderboard and showing
 * them in a sortable list view.
 * @author Eric Harlow and Calvin Bui
 */
public class LeaderboardView extends ListActivity {
    /**
     * List that will host our items and allow us to modify the LeaderboardViewAdapter
     */
    private ArrayList<Ranking> arrayList = null;
    /**
     * Array Adapter that will hold our ArrayList and display the items on the ListView
     */
    private LeaderboardViewAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listleaderboard);
        // initialise arrayList
        arrayList = new ArrayList<Ranking>();
        // retrieve all rankings for this leaderboard from the database
        new RetrieveRankingsTask().execute();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new LeaderboardViewAdapter(this, new int[]{R.layout.leaderboarddragitem}, new int[]{R.id.TextView01}, arrayList);
        setListAdapter(adapter);//new DragNDropAdapter(this,content)
        ListView listView = getListView();

        // make the list sortable by attaching it to a drag, drop and remove listener
        if (listView instanceof DragNDropListView) {
            ((DragNDropListView) listView).setDropListener(mDropListener);
            ((DragNDropListView) listView).setRemoveListener(mRemoveListener);
            ((DragNDropListView) listView).setDragListener(mDragListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leaderboard_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.updateLeaderboard) {
            // save the rankings in the positions specified
            new SaveRankingsTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    // initialise the drop listener
    private DropListener mDropListener =
            new DropListener() {
                public void onDrop(int from, int to) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof LeaderboardViewAdapter) {
                        ((LeaderboardViewAdapter)adapter).onDrop(from, to);
                        getListView().invalidateViews();
                    }
                }
            };

    // initialise the remove listener
    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof LeaderboardViewAdapter) {
                        ((LeaderboardViewAdapter)adapter).onRemove(which);
                        getListView().invalidateViews();
                    }
                }
            };

    // initialise the drag listener
    private DragListener mDragListener =
            new DragListener() {

                int backgroundColor = 0xe0103010;
                int defaultBackgroundColor;

                public void onDrag(int x, int y, ListView listView) {
                    // TODO Auto-generated method stub
                }

                public void onStartDrag(View itemView) {
                    itemView.setVisibility(View.INVISIBLE);
                    defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                    itemView.setBackgroundColor(backgroundColor);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
                    if (iv != null) iv.setVisibility(View.INVISIBLE);
                }

                public void onStopDrag(View itemView) {
                    itemView.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(defaultBackgroundColor);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
                    if (iv != null) iv.setVisibility(View.VISIBLE);
                }
            };

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving rankings from the database.
     */
    private class RetrieveRankingsTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            // retrieve the leaderboard ID from the database and use it to find rankings
            Intent intent = getIntent();
            return LeaderboardDAO.retrieveRankings(intent.getIntExtra(Constants.LEADERBOARD_ID,0));
        }

        @Override
        protected void onPostExecute(String json) {
            // create a new list of ranking objects from the json String
            Type collectionType = new TypeToken<ArrayList<Ranking>>(){}.getType();
            List<Ranking> list = new Gson().fromJson(json, collectionType);
            // add each ranking from the list into the array list
            for (Ranking r : list)
                arrayList.add(r);
            // sort the rankings in order
            sort();
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetInvalidated();
        }
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for saving Rankings into the database.
     */
    private class SaveRankingsTask extends AsyncTask<Void, Void, Void> {
        /**
         * An array to hold the ranking IDs within the leaderboard
         */
        private int[] rankingId;
        /**
         * An array to hold the position/ranking within the leaderboard
         */
        private int[] position;

        protected void onPreExecute() {
            // initialise the arrays to be the length of the arraylist (amount of people)
            rankingId = new int[arrayList.size()+1];
            position = new int[arrayList.size()+1];
            // loop through the lists to store the current ranking order
            for (int i = 0; i < arrayList.size(); i++) {
                rankingId[i] = adapter.getItem(i).getId();
                position[i] = i+1;
            }
        }

        protected Void doInBackground(Void... params) {
            // store the modified rankings into the database
            LeaderboardDAO.updateRankings(rankingId, position);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // return the user to the home screen
            startActivity(new Intent(LeaderboardView.this, LeaderboardHome.class));
        }
    }

    /**
     * Sort the array list based on their ranking
     */
    private void sort() {
        Collections.sort(arrayList, new Comparator<Ranking>() {
            public int compare(Ranking o1, Ranking o2) {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        });
    }
}
