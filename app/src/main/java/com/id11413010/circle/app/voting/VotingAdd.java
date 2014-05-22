package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

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
        option1 = (EditText)findViewById(R.id.questionOption1);
        option2 = (EditText)findViewById(R.id.questionOption2);
    }

    private class CreatePollTask extends AsyncTask<Void, Void, Void> {
        private String circle;
//        private Questions[] questions;
        private Ballot ballot;

        protected void onPreExecute() {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            circle = sp.getString(Constants.CIRCLE, null);

            /*questions = new Questions[2];
            questions[1] = new Questions(option1.getText().toString(), 0);
            questions[2] = new Questions(option1.getText().toString(), 1);*/


            ballot = new Ballot(question.getText().toString(), circle);
        }

        protected Void doInBackground(Void... params) {
            BallotDAO.createPoll(ballot);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
