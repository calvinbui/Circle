/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.dao;

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

    public static void retrievePoll() {

    }

    /**
     * Creates a new question into the database by passing through the question's information.
     * @param question A Question object which holds information
     */
    public static void createQuestion(Question question) {
        // creates a list array which will contain information about the User
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, question.getQuestion())); //name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_VOTES, Integer.toString(question.getVotes()))); //votes
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_POLL, Integer.toString(question.getBallot()))); //poll linked to
        // start a network task with the page to access and information (array list) to process.
        Network.httpConnection("create_question.php", nameValuePairs);
    }
}