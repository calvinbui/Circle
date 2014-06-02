/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.network.Network;
import com.id11413010.circle.app.pojo.Poll;
import com.id11413010.circle.app.pojo.Question;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A data access object class which interacts with a web service to create, retrieve, update and delete
 * objects from the Poll class.
 */
public class PollDAO {
    /**
     * Creates a new poll into the database by passing through Poll information.
     * @param poll A Poll object which holds information
     * @return An integer of the Poll's ID
     */
    public static int createPoll(Poll poll) {
        // creates a list array which will contain information about the Poll
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, poll.getName())); //name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, poll.getCircle())); //circle
        // start a network task with the page to access and information (array list) to process.
        // converts the String returned to an integer.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Integer.valueOf(Network.httpConnection("create_poll.php", nameValuePairs));
    }

    /**
     * Retrieve all polls within the the user's group in the database
     * @param context The application's context
     * @return A JSON String containing all events within the circle
     */
    public static String retrieveAllPolls(Context context) {
        // retrieve the user's circle id and user id from the local shared preferences
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        // creates a list array which will contain information about the leaderboard
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle)); //circle id
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Network.httpConnection("get_all_polls.php", nameValuePairs);
    }

    /**
     * Creates a new question into the database by passing through the question's information.
     * @param question A Question object which holds information
     */
    public static void createQuestion(Question question) {
        // creates a list array which will contain information about the User
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, question.getQuestion())); //name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_POLL, Integer.toString(question.getPoll()))); //poll linked to
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        Network.httpConnection("create_question.php", nameValuePairs);
    }

    /**
     * Retrieve the options relating to the Poll given the Poll ID
     * @param pollID the id of the poll
     * @return a JSON String containing all the options for the poll
     */
    public static String retrievePollQuestions(int pollID) {
        // creates a list array which will contain information about the Poll
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.POLL_ID, Integer.toString(pollID))); // poll id
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Network.httpConnection("get_poll_options.php", nameValuePairs);
    }

    /**
     * Create a new vote for the user into the database
     * @param pollID the poll id
     * @param user the user's id
     * @param option the option selected
     */
    public static void createVote(int pollID, int user, int option) {
        // creates a list array which will contain information about the user's vote
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair(Constants.POLL_ID, Integer.toString(pollID)));
        nameValuePairs.add(new BasicNameValuePair(Constants.USERID, Integer.toString(user)));
        nameValuePairs.add(new BasicNameValuePair(Constants.OPTION_ID, Integer.toString(option)));
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        Network.httpConnection("create_vote.php", nameValuePairs);
    }

    /**
     * Retrieve the amount of votes for the question from the database
     * @param id the selected option's id
     * @return An integer of the amount of votes for the option
     */
    public static int retrieveVotes(int id) {
        // creates a list array which will contain information about the amount of votes for an option
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.OPTION_ID, Integer.toString(id)));
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        return Integer.valueOf(Network.httpConnection("get_votes.php", nameValuePairs));
    }

    /**
     * todo
     * @param poll
     */
    public static void deletePoll(Poll poll) {
        // creates a list array which will contain information about the poll
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.POLL_ID, Integer.toString(poll.getId()))); // event id
        // start a network task with the page to access and information (array list) to process.
        Log.i(Constants.LOG, "Passing array list to network task");
        Network.httpConnection("delete_poll.php", nameValuePairs);
    }
}