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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.PollDAO;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.Question;

import java.util.List;

/**
 * TODO
 */
public class VotingViewAdapter extends ArrayAdapter<Question>{
    private int resource;
    private int memberAmount;
    private int userId;
    private Question item;
    private Question e;

    public VotingViewAdapter(Context context, int resource, List<Question> objects) {
        super(context, resource, objects);
        this.resource = resource;

        SharedPreferences sp = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        userId = sp.getInt(Constants.USERID, 0);

        new GetCircleAmount().execute();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RelativeLayout optionList;
        e = getItem(position);

        if(convertView == null) {
            optionList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, optionList, true);
        } else {
            optionList = (RelativeLayout)convertView;
        }

        TextView option = (TextView)optionList.findViewById(R.id.pollOption);

        final ProgressBar progressBar = (ProgressBar)optionList.findViewById(R.id.votesBar);
        //progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        new GetVotesTask(progressBar).execute(e.getId());

        option.setText(e.getQuestion());
        Button voteBtn = (Button)optionList.findViewById(R.id.voteButton);
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = getItem(position);
                new VoteTask().execute();
                notifyDataSetChanged();
            }
        });
        return optionList;
    }

    private class GetCircleAmount extends AsyncTask<Integer, Void, Integer> {
        protected Integer doInBackground(Integer... params) {
            // retrieves the circle ID from Shared Preferences
            SharedPreferences sp = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            String circle = sp.getString(Constants.CIRCLE, null);
            return Integer.valueOf(UserDAO.retrieveCircleMemberCount(circle));
        }

        protected void onPostExecute(Integer amount) {
            memberAmount = amount;
        }
    }

    private class VoteTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            PollDAO.createVote(item.getPoll(), userId, item.getId());
            return null;
        }
    }

    private class GetVotesTask extends AsyncTask<Integer, Void, Integer> {
        ProgressBar rowProgress;
        public GetVotesTask(ProgressBar progressBar) {
           rowProgress = progressBar;
        }

        protected Integer doInBackground(Integer... params) {
            return PollDAO.retrieveVotes(params[0]);
        }

        protected void onPostExecute(Integer votes) {
            rowProgress.setMax(memberAmount);
            rowProgress.setProgress(votes);
        }
    }
}