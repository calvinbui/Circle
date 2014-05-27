package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
/**
 * A data access object class which interacts with a web service to create, retrieve, update
 * and delete Leaderboard objects from the database.
 */
public class LeaderboardDAO {

    /**
     * Creates a new leaderboard into the database by passing through leaderboard information
     * @param context The application's context
     * @param name The name of the leaderboard to create
     */
    public static String createLeaderboard(Context context, String name) {
        // retrieve the user's circle id and user id from the local shared preferences
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        int userId = sp.getInt(Constants.USERID, 0);
        // creates a list array which will contain information about the leaderboard
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle)); //circle id
        nameValuePairs.add(new BasicNameValuePair(Constants.USERID, Integer.toString(userId))); //user id
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LEADERBOARD_NAME, name)); //leadeboard name
        // start a network task with the page to access and information (array list) to process.
        return Network.httpConnection("create_leaderboard.php", nameValuePairs);
    }

    /**
     * Retrieve leaderboards from the database by passing through the user's circle id
     * @param context The application's context
     * @return A JSON String containing leaderboard information
     */
    public static String retrieveAllLeaderboards(Context context) {
        // retrieve the user's circle id and user id from the local shared preferences
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        // creates a list array which will contain information about the leaderboard
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle)); //circle id
        // start a network task with the page to access and information (array list) to process.
        return Network.httpConnection("get_all_leaderboards.php", nameValuePairs);
    }
}
