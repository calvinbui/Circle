package com.id11413010.circle.app.dao;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.Network;
import com.id11413010.circle.app.pojo.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CalvinLaptop on 20/05/2014.
 */
public class UserDAO {
    public static void createUser(User user) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_FIRSTNAME, user.getFirstName()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_LASTNAME, user.getLastName()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_EMAIL, user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_PASSWORD, user.getPassword()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DB_CIRCLE, user.getCircle()));
        Network.httpConnection(Constants.DB_URL + "register.php", nameValuePairs);
    }
}
