package com.id11413010.circle.app.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.id11413010.circle.app.HomeScreen;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.User;

import java.util.ArrayList;


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
    }

    public class RetrieveUsersTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            UserDAO.retrieveAllUsers(Friends.this, arrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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
