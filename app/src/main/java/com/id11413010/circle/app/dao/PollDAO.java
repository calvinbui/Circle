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
 * TODO
 */
public class PollDAO {

    public static int createBallot(Poll poll) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        // Ballot name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, poll.getName()));
        // Ballot circle
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, poll.getCircle()));
        String s = Network.httpConnection("create_poll.php", nameValuePairs);
        return Integer.valueOf(s);
    }

    public static void retrievePoll() {

    }

    public static void createQuestion(Question question) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, question.getQuestion()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_VOTES, Integer.toString(question.getVotes())));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_POLL, Integer.toString(question.getBallot())));
        Network.httpConnection("create_question.php", nameValuePairs);
    }
}
