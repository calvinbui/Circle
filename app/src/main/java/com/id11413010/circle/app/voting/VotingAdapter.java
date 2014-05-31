package com.id11413010.circle.app.voting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.pojo.Poll;

import java.util.List;

/**
 * An adapter which acts as a bridge between an activity and database.
 * Retrieves data form the database and updates a view for each item within a data set.
 * Responsible for displaying Polls.
 */
public class VotingAdapter extends ArrayAdapter<Poll> {
    /**
     * The index to a layout view
     */
    private int resource;

    /**
     * Initialise the adapter with a context, layout resource and arraylist.
     */
    public VotingAdapter(Context context, int resource, List<Poll> poll) {
        super(context, resource, poll);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // the layout of the list view containing widgets
        ViewHolder holder;
        // the object at the current array position
        Poll p = getItem(position);

        /*
        if the list is empty, inflate the layout with the list object after initialising
        the layout.
         */
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
            //finds and stores a view that was identified by the id attribute
            holder.question = (TextView)convertView.findViewById(R.id.votingList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        // set the question
        holder.question.setText(p.getName());
        // return the row
        return convertView;
    }

    /**
     * A ViewHolder design pattern. Caches widgets.
     */
    private static class ViewHolder {
        private TextView question;
    }
}
