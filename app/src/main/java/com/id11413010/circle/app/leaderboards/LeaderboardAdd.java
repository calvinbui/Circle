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

public class LeaderboardAdd extends Activity {

    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_add);
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
            new CreateLeaderboardTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    private class CreateLeaderboardTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            LeaderboardDAO.createLeaderboard(LeaderboardAdd.this, name.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            startActivity(new Intent(LeaderboardAdd.this, LeaderboardHome.class));
        }
    }
}
