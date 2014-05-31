package com.id11413010.circle.app.voting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.PollDAO;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.Question;

import java.util.List;

/**
 *
 */
public class VotingViewAdapter extends ArrayAdapter<Question>{
    /**
     * The index to a layout view
     */
    private int resource;
    /**
     * The amount of members within the circle
     */
    private int memberAmount;

    /**
     * Initialise the adapter with a context, layout resource and arraylist.
     */
    public VotingViewAdapter(Context context, int resource, List<Question> objects) {
        super(context, resource, objects);
        this.resource = resource;
        // get the amount of users within the circle
        new GetCircleAmount().execute();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // the layout of the list view containing widgets
        ViewHolder holder;
        // the object at the current array position
        Question e = getItem(position);

        /*
        if the list is empty, inflate the layout with the list object after initialising
        the layout.
         */
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
            //finds and stores a view that was identified by the id attribute
            holder.option = (TextView)convertView.findViewById(R.id.pollOption);
            holder.progressBar = (ProgressBar)convertView.findViewById(R.id.votesBar);
            holder.voteBtn = (Button)convertView.findViewById(R.id.voteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // get the amount of votes for each progress bar
        new GetVotesTask(holder.progressBar).execute(e.getId());

        // set the option's name/question
        holder.option.setText(e.getQuestion());

        // set a listener onto the button to allow users to vote
        holder.voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the question at the row
                Question item = getItem(position);
                // update the amount of votes for the question
                new VoteTask(item).execute();
            }
        });
        // return the row
        return convertView;
    }

    /**
     * A ViewHolder design pattern. Caches widgets.
     */
    private static class ViewHolder {
        private TextView option;
        private Button voteBtn;
        private ProgressBar progressBar;
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving the amount of users in a circle from the database.
     */
    private class GetCircleAmount extends AsyncTask<Integer, Void, Integer> {
        protected Integer doInBackground(Integer... params) {
            // retrieves the circle ID from Shared Preferences
            SharedPreferences sp = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            String circle = sp.getString(Constants.CIRCLE, null);
            // returns the amount of users
            return Integer.valueOf(UserDAO.retrieveCircleMemberCount(circle));
        }

        protected void onPostExecute(Integer amount) {
            // set the amount of users to the field for later use
            memberAmount = amount;
        }
    }

    /**
    * An AsyncTask which captures the information inputted by the User and sends it via Internet
    * to the a web service to be added into the database. Separates network activity from the main
    * thread. Responsible for inserting votes into the database.
    */
    private class VoteTask extends AsyncTask<Void, Void, Void> {
        // the poll option chosen
        private Question i;

        private VoteTask(Question i) {
            this.i = i;
        }

        protected Void doInBackground(Void... params) {
            // get the user's id from the local shared preferences
            SharedPreferences sp = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            int userId = sp.getInt(Constants.USERID, 0);
            // update the database for the chosen option chosen
            PollDAO.createVote(i.getPoll(), userId, i.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // notify the adapter that the underlying data has changed to update its view.
            notifyDataSetChanged();
        }
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving votes from the database.
     */
    private class GetVotesTask extends AsyncTask<Integer, Void, Integer> {
        // a progress bar representing the amount of votes on a question
        private ProgressBar rowProgress;

        public GetVotesTask(ProgressBar progressBar) {
           rowProgress = progressBar;
        }

        protected Integer doInBackground(Integer... params) {
            // get the amount of votes from the database
            return PollDAO.retrieveVotes(params[0]);
        }

        protected void onPostExecute(Integer votes) {
            // set the maximum and progress of the progress bar
            rowProgress.setMax(memberAmount);
            rowProgress.setProgress(votes);
        }
    }
}