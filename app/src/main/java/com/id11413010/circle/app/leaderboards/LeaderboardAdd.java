package com.id11413010.circle.app.leaderboards;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.LeaderboardDAO;

/**
 * This class is used to create leaderboards. Users enter a name for
 * the leaderboard and it is added to the database along with a default
 * list of rankings.
 */
public class LeaderboardAdd extends Activity {

    /**
     * An EditText for the leaderboard name
     */
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_add);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //finds and stores a view that was identified by the id attribute
        name = (EditText)findViewById(R.id.leaderboardNewName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leaderboard_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.createLeaderboard) {
            /*
            when the user clicks on this option, it will execute an AsyncTask to add the
            leaderboard to the database.
             */
            new CreateLeaderboardTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * An AsyncTask which captures the information inputed by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread.
     *
     * Creates a new leaderboard into the database.
     */
    private class CreateLeaderboardTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            // create the leaderboard with the name specified
            LeaderboardDAO.createLeaderboard(LeaderboardAdd.this, name.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // return the user to the leadebroard home page
            startActivity(new Intent(LeaderboardAdd.this, LeaderboardHome.class));
            finish();
        }
    }
}
