/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.dao;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
import com.id11413010.circle.app.pojo.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A data access object class which interacts with a web service to create, retrieve, update
 * and delete objects from the Event class.
 */
public class EventDAO {
    /**
     * Creates a new event into the database by passing through Event information.
     * @param event A Event object which holds information
     */
    public static void addEvent(Event event) {
        // creates a list array which will contain information about the User

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, event.getName())); // name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_DESCRIPTION, event.getDescription())); // description
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LOCATION, event.getLocation())); // location
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_STARTDATE, event.getStartDate())); // start date
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_ENDDATE, event.getEndDate())); // end date
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_STARTTIME, event.getStartTime())); // start time
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_ENDTIME, event.getEndTime())); // end time
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, event.getCircle())); // circle
        // start a network task with the page to access and information (array list) to process.
        Network.httpConnection("create_event.php", nameValuePairs);
    }

    public static String retrieveEvents() {
        return Network.httpConnection("get_events.php", new ArrayList<NameValuePair>(0));
    }
}
