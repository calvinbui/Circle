/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
import com.id11413010.circle.app.pojo.Event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
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
        Network.httpConnection("create_event.php", nameValuePairs);
    }

    /**
     * Retrieve all events within the user's circle.
     * @return A Json String containing all the group's events
     */
    public static ArrayList<Event> retrieveEvents(Context context, ArrayList<Event> arrayList) {
        // creates a list array which will contain information about the User
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, sp.getString(Constants.CIRCLE, null))); // circle
        // start a network task with the page to access and information (array list) to process.
        String json = Network.httpConnection("get_events.php", nameValuePairs);
        // create a new list of event objects from the json String
        Type collectionType = new TypeToken<ArrayList<Event>>(){}.getType();
        List<Event> list = new Gson().fromJson(json, collectionType);
        // add each event from the list into the array list
        for (Event e : list)
            arrayList.add(e);
        return arrayList;
    }
}