package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.id11413010.circle.app.R;

public class VotingAddQuestion extends Activity {

    private Button finish;
    private Button addQuestion;
    private EditText question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_add_question);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.voting_add_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.createVoting) {
            // execute AsyncTask AND then open the main Event page
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        // execute AsyncTask but also open a new activity to add another one.
        // do not make the AsyncTask start activity because what if we're done?!
    }
}
