package com.id11413010.circle.app.voting;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView question;
    private ArrayList<Question> arrayList = null;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_view);
        intent = getIntent();
        question = (TextView)findViewById(R.id.pollQuestion);
        question.setText(intent.getStringExtra(Constants.POLL_NAME));
        listView = (ListView)findViewById(R.id.questionList);
        // initialise arrayList
        arrayList = new ArrayList<Question>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new VotingViewAdapter(this, R.layout.listoptions, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        new RetrieveOptionsTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.voting_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            adapter.notifyDataSetChanged();
        }
    }
}
