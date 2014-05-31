/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.PollDAO;
import com.id11413010.circle.app.pojo.Question;

/**
 * This class is used to create a new questions for polls. Users write a new question which is
 * then created within the database. The user has the object of adding another question or finishing.
 */
public class VotingAddQuestion extends Activity {
    /**
     * An EditText representing the poll question
     */
    private EditText question;
    /**
     * A integer representing the group circle ID
     */
    private int pollID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_add_question);
        // capture the Intent which started this activity
        Intent intent = getIntent();
        // extra the integer placed in the intent
        pollID = intent.getIntExtra(Constants.DB_POLL, 0);
        //finds and stores a view that was identified by the id attribute
        question = (EditText)findViewById(R.id.question);
        Log.i(Constants.LOG, "Started Voting Add Question");
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
        if (id == R.id.finishCreatingQuestions) {
            // start the main Voting page if this menu button is pressed
            startActivity(new Intent(this, Voting.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Add the poll option upon pressing the button and restarts the activity.
     */
    public void addQuestion(View v) {
        // validates the user has entered a poll option, otherwise Toasts the user to do so
        if (question.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.enterOption), Toast.LENGTH_SHORT).show();
        } else {
            // execute an AsyncTask to add the poll option
            new CreateQuestionTask().execute();
            // create a new intent to restart this activity
            Intent i  = new Intent(this, VotingAddQuestion.class);
            // insert the Poll ID into this intent
            i.putExtra(Constants.DB_POLL, pollID);
            // start the activity given the intent
            startActivity(i);
        }
    }

    /**
     * Add the poll option and closes the activity. Returns the user to the main voting page
     */
    public void addQuestionFinish(View v) {
        // validates the user has entered a poll option, otherwise Toasts the user to do so
        if (question.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.enterOption), Toast.LENGTH_SHORT).show();
        } else {
            // execute an AsyncTask to add the poll option
            new CreateQuestionTask().execute();
            // start the Voting activity
            startActivity(new Intent(this, Voting.class));
        }
    }

    /**
     * An AsyncTask which captures the information inputed by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread.
     *
     * Creates a poll option into the database.
     */
    private class CreateQuestionTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            // creates a new poll option given the name, default amount of votes and poll ID.
            Question q = new Question(question.getText().toString(), pollID, null);
            // pass the object to the data-access-object class to add it to the database
            PollDAO.createQuestion(q);
            return null;
        }
    }
}