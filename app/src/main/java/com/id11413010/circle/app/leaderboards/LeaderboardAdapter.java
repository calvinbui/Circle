package com.id11413010.circle.app.leaderboards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.pojo.Leaderboard;

import java.util.List;

/**
 * An adapter which acts as a bridge between an activity and database.
 * Retrieves data form the database and updates a view for each item within a data set.
 * Responsible for displaying Leaderboards.
 */
public class LeaderboardAdapter extends ArrayAdapter<Leaderboard> {
    /**
     * The index to a layout view
     */
    private int resource;
    /**
     * Initialise the adapter with a context, layout resource and arraylist.
     */
    public LeaderboardAdapter(Context context, int resource, List<Leaderboard> Leaderboard) {
        super(context, resource, Leaderboard);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // the layout of the list view containing widgets
        ViewHolder holder;
        // the object at the current array position
        Leaderboard l = getItem(position);

        /*
        if the list is empty, inflate the layout with the list object after initialising
        the layout.
         */
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
            //finds and stores a view that was identified by the id attribute
            holder.question = (TextView)convertView.findViewById(R.id.leaderboardNameList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        //finds and stores a view that was identified by the id attribute

        // set the leaderboard name
        holder.question.setText(l.getName());
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
