package com.id11413010.circle.app.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardDAO {

    public static void createLeaderboard(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        int userId = sp.getInt(Constants.USERID, 0);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
        nameValuePairs.add(new BasicNameValuePair(Constants.USERID, Integer.toString(userId)));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LEADERBOARD_NAME, name));

        Network.httpConnection("create_leaderboard.php", nameValuePairs);
    }

    public static String retrieveAllLeaderboards(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String circle = sp.getString(Constants.CIRCLE, null);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, circle));
        return Network.httpConnection("get_all_leaderboards.php", nameValuePairs);
    }
}
