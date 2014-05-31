package com.id11413010.circle.app.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.HomeScreen;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is used for listing friends.
 * Friends are retrieved from a database and shown within a listview.
 */
public class Friends extends Activity {
    /**
     * ListView that will hold our items references back to main.xml
     */
    private ListView listView;
    /**
     * Array Adapter that will hold our ArrayList and display the items on the ListView
     */
    private FriendAdapter adapter;
    /**
     * List that will host our items and allow us to modify the UserAdapter
     */
    private ArrayList<User> arrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // initialise list view
        listView = (ListView)findViewById(R.id.friendList);
        // initialise arrayList
        arrayList = new ArrayList<User>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new FriendAdapter(this, R.layout.listfriends, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        new RetrieveUsersTask().execute();
        Log.i(Constants.LOG, "Started Friends");
    }

    public class RetrieveUsersTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            return UserDAO.retrieveAllUsers(Friends.this);
        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> list = new Gson().fromJson(json, collectionType);
            for (User u : list)
                arrayList.add(u);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }
}
