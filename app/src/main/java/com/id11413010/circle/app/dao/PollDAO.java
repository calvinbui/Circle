/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
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
        return Integer.valueOf(Network.httpConnection("create_poll.php", nameValuePairs));
    }

    public static String retrieveAllPolls(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
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
        Network.httpConnection("create_question.php", nameValuePairs);
    }

    public static String retrievePollQuestions(int pollID) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.POLL_ID, Integer.toString(pollID)));
        return Network.httpConnection("get_poll_options.php", nameValuePairs);
    }

    public static String createVote(int pollID, int user, int option) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair(Constants.POLL_ID, Integer.toString(pollID)));
        nameValuePairs.add(new BasicNameValuePair(Constants.USERID, Integer.toString(user)));
        nameValuePairs.add(new BasicNameValuePair(Constants.OPTION_ID, Integer.toString(option)));
        return Network.httpConnection("create_vote.php", nameValuePairs);
    }

    public static int retrieveVotes(int id) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.OPTION_ID, Integer.toString(id)));
        return Integer.valueOf(Network.httpConnection("get_votes.php", nameValuePairs));
    }
}