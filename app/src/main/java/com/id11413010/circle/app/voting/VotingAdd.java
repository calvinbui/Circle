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
import com.id11413010.circle.app.dao.BallotDAO;
import com.id11413010.circle.app.pojo.Ballot;

public class VotingAdd extends Activity {

    private EditText question;
    private EditText option1;
    private EditText option2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_add);
        question = (EditText)findViewById(R.id.question);
    }

    public void createBallot(View v) {
        if (question.getText().toString().matches(""))
            Toast.makeText(getApplicationContext(), getString(R.string.blankEventName), Toast.LENGTH_SHORT).show();
        else
            new CreateBallotTask().execute();
    }

    private class CreateBallotTask extends AsyncTask<Void, Void, Void> {
        private String circle;
        private Ballot ballot;
        private int ballotID;

        protected void onPreExecute() {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            circle = sp.getString(Constants.CIRCLE, null);
            ballot = new Ballot(question.getText().toString(), circle);
        }

        protected Void doInBackground(Void... params) {
            ballotID = BallotDAO.createBallot(ballot);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(VotingAdd.this, VotingAddQuestion.class);
            i.putExtra(Constants.DB_POLL, ballotID);
            startActivity(i);
        }
    }
}
