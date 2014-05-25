package com.id11413010.circle.app.voting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.pojo.Poll;

import java.util.List;

public class VotingAdapter extends ArrayAdapter<Poll> {
    private int resource;

    public VotingAdapter(Context context, int resource, List<Poll> poll) {
        super(context, resource, poll);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout pollList;
        Poll p = getItem(position);

        if(convertView == null) {
            pollList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, pollList, true);
        } else {
            pollList = (RelativeLayout)convertView;
        }

        TextView question = (TextView)pollList.findViewById(R.id.votingList);
        question.setText(p.getName());

        return pollList;
    }
}
