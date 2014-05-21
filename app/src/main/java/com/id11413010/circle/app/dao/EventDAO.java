package com.id11413010.circle.app.dao;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
import com.id11413010.circle.app.pojo.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CalvinLaptop on 20/05/2014.
 */
public class EventDAO {
    public static void addEvent(Event event) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
        // name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, event.getName()));
        // Events description
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_DESCRIPTION, event.getDescription()));
        // Events location
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LOCATION, event.getLocation()));
        // Events start date
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_STARTDATE, event.getStartDate()));
        // Events end date
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_ENDDATE,event.getEndDate()));
        // Events start time
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_STARTTIME, event.getStartTime()));
        // Events end time
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_ENDTIME, event.getEndTime()));
        // User Circle ID
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, event.getCircle()));
        Network.httpConnection("create_event.php", nameValuePairs);
    }
}
