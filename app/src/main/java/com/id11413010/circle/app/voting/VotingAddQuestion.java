package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.BallotDAO;
import com.id11413010.circle.app.pojo.Question;

public class VotingAddQuestion extends Activity {
    private EditText question;
    private int pollID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_add_question);
        String s = getIntent().getExtras().getString(Constants.DB_POLL); //TODO convert to int?
        pollID = Integer.valueOf(s);
        question = (EditText)findViewById(R.id.question);
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

    public void addQuestion(View v) {
        // capture the ID via the Voting name add first...
        new CreateQuestionTask().execute();
        Intent i  = new Intent(this, VotingAddQuestion.class);
        i.putExtra(Constants.DB_POLL, pollID);
        startActivity(i);
    }

    public void addQuestionFinish(View v) {
        new CreateQuestionTask().execute();
        startActivity(new Intent(this, Voting.class));
    }

    private class CreateQuestionTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            Question q = new Question(question.getText().toString(), 0, pollID);
            BallotDAO.createQuestion(q);
            return null;
        }
    }
}