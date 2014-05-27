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

public class LeaderboardAdapter extends ArrayAdapter<Leaderboard> {
    private int resource;

    public LeaderboardAdapter(Context context, int resource, List<Leaderboard> Leaderboard) {
        super(context, resource, Leaderboard);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout LeaderboardList;
        Leaderboard l = getItem(position);

        if(convertView == null) {
            LeaderboardList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, LeaderboardList, true);
        } else {
            LeaderboardList = (RelativeLayout)convertView;
        }

        TextView question = (TextView)LeaderboardList.findViewById(R.id.leaderboardNameList);
        question.setText(l.getName());

        return LeaderboardList;
    }
}
