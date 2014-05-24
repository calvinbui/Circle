package com.id11413010.circle.app.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.pojo.User;

import java.util.List;

/**
 * TODO
 */
public class FriendAdapter extends ArrayAdapter<User> {
    int resource;
    /**
     * Initialise the adapter
     */
    public FriendAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout userList;
        User u = getItem(position);

        if(convertView == null) {
            userList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, userList, true);
        } else {
            userList = (RelativeLayout)convertView;
        }

        TextView name = (TextView)userList.findViewById(R.id.userListName);
        name.setText(u.getFirstName() + " " + u.getLastName());
        return userList;
    }

}
