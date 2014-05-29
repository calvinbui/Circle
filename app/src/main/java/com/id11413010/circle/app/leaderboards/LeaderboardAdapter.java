package com.id11413010.circle.app.leaderboards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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
        RelativeLayout LeaderboardList;
        // the object at the current array position
        Leaderboard l = getItem(position);

        /*
        if the list is empty, inflate the layout with the list object after initialising
        the layout.
         */
        if(convertView == null) {
            LeaderboardList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, LeaderboardList, true);
        } else {
            LeaderboardList = (RelativeLayout)convertView;
        }

        //finds and stores a view that was identified by the id attribute
        TextView question = (TextView)LeaderboardList.findViewById(R.id.leaderboardNameList);
        // set the leaderboard name
        question.setText(l.getName());
        // return the row
        return LeaderboardList;
    }
}
