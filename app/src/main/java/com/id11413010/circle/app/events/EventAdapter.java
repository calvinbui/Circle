package com.id11413010.circle.app.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11413010.circle.app.R;
import com.id11413010.circle.app.pojo.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TODO
 */
public class EventAdapter extends ArrayAdapter<Event>{
    int resource;
    /**
     * Initialise the adapter
     */
    public EventAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout eventList;
        Event e = getItem(position);

        if(convertView == null) {
            eventList = new RelativeLayout(getContext());
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, eventList, true);
        } else {
            eventList = (RelativeLayout)convertView;
        }

        TextView monthYear = (TextView)eventList.findViewById(R.id.eventListMonthYear);
        TextView day = (TextView)eventList.findViewById(R.id.eventListDay);
        TextView name = (TextView)eventList.findViewById(R.id.eventListName);
        TextView time = (TextView)eventList.findViewById(R.id.eventListTime);
        TextView location = (TextView)eventList.findViewById(R.id.eventListLocation);

        monthYear.setText(getMonthYearFormatDate(e));
        day.setText(getDayFormatDate(e));
        name.setText(e.getName());
        time.setText(formatTime(e.getStartTime()) + " - " + formatTime(e.getEndTime()));
        location.setText(e.getLocation());

        return eventList;
    }

    private String getDayFormatDate(Event event) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getStartDate());
            DateFormat df = new SimpleDateFormat("d");
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMonthYearFormatDate(Event event) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getStartDate());
            DateFormat df = new SimpleDateFormat("MMM-yyyy");
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatTime(String time) {
        try {
            Date date = new SimpleDateFormat("HH:mm:ss").parse(time);
            DateFormat df = new SimpleDateFormat("hh:mm aaa");
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
