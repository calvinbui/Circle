package com.id11413010.circle.app.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.pojo.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An adapter which acts as a bridge between an activity and database.
 * Retrieves data form the database and updates a view for each item within a data set.
 * Responsible for displaying Events.
 */
public class EventAdapter extends ArrayAdapter<Event>{
    /**
     * The index to a layout view
     */
    private int resource;
    /**
     * Initialise the adapter with a context, layout resource and arraylist.
     */
    public EventAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // the layout of the list view containing widgets
        ViewHolder holder;
        // the object at the current array position
        Event e = getItem(position);

        /*
        if the list is empty, inflate the layout with the list object after initialising
        the layout.
         */
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
            //finds and stores a view that was identified by the id attribute
            holder.monthYear = (TextView)convertView.findViewById(R.id.eventListMonthYear);
            holder.day = (TextView)convertView.findViewById(R.id.eventListDay);
            holder.name = (TextView)convertView.findViewById(R.id.eventListName);
            holder.time = (TextView)convertView.findViewById(R.id.eventListTime);
            holder.location = (TextView)convertView.findViewById(R.id.eventListLocation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        // set the month
        holder.monthYear.setText(formatDate(e, "MMM-yyyy"));
        // set the day
        holder.day.setText(formatDate(e, "d"));
        // set the name
        holder.name.setText(e.getName());
        // set the time
        holder.time.setText(formatTime(e.getStartTime()) + " - " + formatTime(e.getEndTime()));
        // set the location
        holder.location.setText(e.getLocation());
        // return the row
        return convertView;
    }

    /**
     * Format the date into a time readable by humans (dd-mm-yyyy)
     * @param event The event object
     * @param format the type of date to return
     * @return A String formatted date
     */
    private String formatDate(Event event, String format) {
        try {
            // create a new date from the event's start date
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getStartDate());
            // format the date into the specified format
            DateFormat df = new SimpleDateFormat(format);
            // return the date as a String
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Format the time to include a AM/PM
     * @param time The time to format
     * @return A String of the time with AM or PM appended
     */
    private String formatTime(String time) {
        try {
            // create a new time from the event's start time
            Date date = new SimpleDateFormat("HH:mm:ss").parse(time);
            // format the time to include AM or PM
            DateFormat df = new SimpleDateFormat("hh:mm aaa");
            // return the time as a String
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A ViewHolder design pattern. Caches widgets.
     */
    private static class ViewHolder {
        private TextView monthYear;
        private TextView day;
        private TextView name;
        private TextView time;
        private TextView location;
    }
}