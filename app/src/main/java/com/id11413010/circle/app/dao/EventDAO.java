/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.network.Network;
import com.id11413010.circle.app.pojo.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A data access object class which interacts with a web service to create, retrieve, update
 * and delete Event objects from the database.
 */
public class EventDAO {
    /**
     * Creates a new event into the database by passing through Event information.
     * @param event A Event object which holds information
     */
    public static void createEvent(Event event) {
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
        Log.i(Constants.LOG, "Passing array list to network task");
        Network.httpConnection("create_event.php", nameValuePairs);
    }

    /**
     * Retrieve all events within the user's circle.
     * @return A Json String containing all the group's events
     */
    public static String retrieveEvents(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        // creates a list array which will contain information about the event
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, sp.getString(Constants.CIRCLE, null))); // circle
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Network.httpConnection("get_events.php", nameValuePairs);
    }

    /**
     * Delete and event from the user's circle.
     * @param event The event to delete
     */
    public static void deleteEvent(Event event) {
        // creates a list array which will contain information about the event
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.EVENT_ID, Integer.toString(event.getId()))); // event id
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        Network.httpConnection("delete_event.php", nameValuePairs);
    }

    /**
     * Retrieve all past events within the user's circle.
     * @return A Json String containing all the group's events
     */
    public static String retrievePastEvents(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        // creates a list array which will contain information about the event
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, sp.getString(Constants.CIRCLE, null))); // circle
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Network.httpConnection("get_events_past.php", nameValuePairs);
    }
}