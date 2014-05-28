package com.id11413010.circle.app.leaderboards;

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
import com.id11413010.circle.app.dao.LeaderboardDAO;
import com.id11413010.circle.app.pojo.Leaderboard;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        listView = (ListView)findViewById(R.id.leaderboardList);
        // initialise arrayList
        arrayList = new ArrayList<Leaderboard>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new LeaderboardAdapter(this, R.layout.listleaderboardsall, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        new RetrieveLeaderboardTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
                Leaderboard item = (Leaderboard)adapterView.getItemAtPosition(position);
                Intent i = new Intent(LeaderboardHome.this, LeaderboardView.class);
                i.putExtra(Constants.LEADERBOARD_ID, item.getId());
                i.putExtra(Constants.LEADERBOARD_NAME, item.getName());
                startActivity(i);
            }
        });
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

    public class RetrieveLeaderboardTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            return LeaderboardDAO.retrieveAllLeaderboards(LeaderboardHome.this);

        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<Leaderboard>>(){}.getType();
            List<Leaderboard> list = new Gson().fromJson(json, collectionType);
            for (Leaderboard l : list)
                arrayList.add(l);
            adapter.notifyDataSetChanged();
        }
    }
}
