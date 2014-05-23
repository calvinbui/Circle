/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.PollDAO;
import com.id11413010.circle.app.pojo.Poll;

/**
 * This class is used to create a new Polls. Users can fill out the details of poll event which is
 * then created within the database. The poll requires a name and circle it belongs to. When the
 * poll has been created, the user is redirected to an activity to add questions to the poll.
 */
public class VotingAdd extends Activity {
    /**
     * An EditText representing the Poll's name
     */
    private EditText question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_add);
        //finds and stores a view that was identified by the id attribute
        question = (EditText)findViewById(R.id.question); //poll name
    }

    /**
     * Executes an AsyncTask to add the Poll to the database.
     */
    public void createPoll(View v) {
        // if nothing entered in, Toast the user to enter a name
        if (question.getText().toString().matches(""))
            Toast.makeText(getApplicationContext(), getString(R.string.noPollQuestion), Toast.LENGTH_SHORT).show();
        else
            // execute the AsyncTask
            new CreatePollTask().execute();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(VotingAdd.this, Voting.class));
        finish();
    }

    private class CreatePollTask extends AsyncTask<Void, Void, Void> {
        /**
         * A String representing the user's circle
         */
        private String circle;
        /**
         * A Poll object to hold information about the poll
         */
        private Poll poll;
        /**
         * A integer containing the Poll's ID
         */
        private int pollID;

        protected void onPreExecute() {
            // get the user's circle from Shared Preferences
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            circle = sp.getString(Constants.CIRCLE, null);
            // create a new poll object with information entered by the user and the circle id
            poll = new Poll(question.getText().toString(), circle);
        }

        protected Void doInBackground(Void... params) {
            // pass the object to the data-access-object class to add it to the database and return the
            // id assigned to it in the database.
            pollID = PollDAO.createPoll(poll);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // create a new intent to start an activity to add Questions to the Poll
            Intent i = new Intent(VotingAdd.this, VotingAddQuestion.class);
            // place the Poll ID from the database into the intent for questions to be linked to this poll
            i.putExtra(Constants.DB_POLL, pollID);
            // start the activity given the intent
            startActivity(i);
        }
    }
}
