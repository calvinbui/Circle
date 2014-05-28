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
 * An adapter which acts as a bridge between an activity and database.
 * Retrieves data form the database and updates a view for each item within a data set.
 * Responsible for displaying Friends.
 */
public class FriendAdapter extends ArrayAdapter<User> {
    /**
     * The index to a layout view
     */
    private int resource;
    /**
     * Initialise the adapter with a context, layout resource and arraylist.
     */
    public FriendAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // the layout of the list view containing widgets
        RelativeLayout userList;
        // the object at the current array position
        User u = getItem(position);

        /*
        if the list is empty, inflate the layout with the list object after initialising
        the layout.
         */
        if(convertView == null) {
            userList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, userList, true);
        } else {
            userList = (RelativeLayout)convertView;
        }
        //finds and stores a view that was identified by the id attribute
        TextView name = (TextView)userList.findViewById(R.id.userListName);
        //set the name
        name.setText(u.getFirstName() + " " + u.getLastName());
        //return the row
        return userList;
    }

}
