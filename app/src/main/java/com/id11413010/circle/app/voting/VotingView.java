package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.PollDAO;
import com.id11413010.circle.app.pojo.Question;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity which displays the poll options for the poll. Users can vote on poll options and
 * see other votes.
 */
public class VotingView extends Activity {
    /**
     * ListView that will hold our items references back to main.xml
     */
    private ListView listView;
    /**
     * Array Adapter that will hold our ArrayList and display the items on the ListView
     */
    private VotingViewAdapter adapter;
    /**
     * List that will host our items and allow us to modify the VotingAdapter
     */
    private ArrayList<Question> arrayList = null;
    /**
     * The previous intent to open this activity
     */
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // get the previous intent
        intent = getIntent();
        // set the title
        setTitle(intent.getStringExtra(Constants.POLL_NAME));
        // set the question to the textview
        TextView question = (TextView)findViewById(R.id.pollQuestion);
        question.setText(intent.getStringExtra(Constants.POLL_NAME));
        listView = (ListView)findViewById(R.id.questionList);
        // initialise arrayList
        arrayList = new ArrayList<Question>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new VotingViewAdapter(this, R.layout.listoptions, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        new RetrieveOptionsTask().execute();
        Log.i(Constants.LOG, "Started Voting View");
    }

    private class RetrieveOptionsTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            return PollDAO.retrievePollQuestions(intent.getIntExtra(Constants.POLL_ID, 0));
        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<Question>>(){}.getType();
            List<Question> list = new Gson().fromJson(json, collectionType);
            for (Question q : list)
                arrayList.add(q);
            adapter.notifyDataSetInvalidated();
        }
    }
}
