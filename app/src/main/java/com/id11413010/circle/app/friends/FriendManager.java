package com.id11413010.circle.app.friends;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.pojo.User;

import java.util.ArrayList;

/**
 * Created by Calvin on 11/05/2014.
 */
public class FriendManager {
    private ArrayList<User> users;
    private SQLiteDatabase db;
    private FriendDatabaseHelper dbHelper;

    public FriendManager(Context context) {
        dbHelper = new FriendDatabaseHelper(context);
        users = new ArrayList<User>();
    }

    public void createFriend(String firstName, String lastName) {
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(Constants.COLUMN_FIRSTNAME, firstName);
        v.put(Constants.COLUMN_LASTNAME, lastName);

        db.insert(Constants.FRIENDS, null, v);

        dbHelper.close();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    private int nextId() {
        return users.get(users.size() - 1).getId() + 1;
    }
}
