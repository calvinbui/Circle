package com.id11413010.circle.app.dao;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
import com.id11413010.circle.app.pojo.Ballot;
import com.id11413010.circle.app.pojo.Question;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CalvinLaptop on 21/05/2014.
 */
public class BallotDAO {

    public static int createBallot(Ballot ballot) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        // Ballot name
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, ballot.getName()));
        // Ballot circle
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, ballot.getCircle()));
        String s = Network.httpConnection("create_poll.php", nameValuePairs);
        return Integer.valueOf(s);
    }

    public static void retrievePoll() {

    }

    public static void createQuestion(Question question) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_NAME, question.getQuestion()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_VOTES, question.getVotes() + ""));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_POLL, question.getBallot() + ""));
        Network.httpConnection("create_question.php", nameValuePairs);
    }
}
