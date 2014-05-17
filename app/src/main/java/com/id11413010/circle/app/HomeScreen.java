package com.id11413010.circle.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.id11413010.circle.app.events.Event;
import com.id11413010.circle.app.friends.Friend_List;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String firstName = sp.getString(Constants.FIRSTNAME, getText(R.string.tester).toString());
        Toast.makeText(getApplicationContext(), getText(R.string.welcomeMsg).toString() + firstName, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void openFriends(View v) {
        startActivity(new Intent(HomeScreen.this, Friend_List.class));
    }

    public void openEvents(View v) {
        startActivity(new Intent(HomeScreen.this, Event.class));
    }

}
