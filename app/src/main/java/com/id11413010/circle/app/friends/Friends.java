package com.id11413010.circle.app.friends;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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
}
