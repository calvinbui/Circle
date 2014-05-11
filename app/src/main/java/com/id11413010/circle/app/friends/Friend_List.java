package com.id11413010.circle.app.friends;

import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import com.id11413010.circle.app.R;


public class Friend_List extends ListActivity {

    private FriendDatabaseHelper db;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        db = new FriendDatabaseHelper(this);
        adapter = new SimpleCursorAdapter(this, R.layout.activity_friend, db.getUsers(), db.from(), db.to());
        setListAdapter(adapter);
    }

    private void initialise() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addFriendMenu) {
            startActivity(new Intent(this, Friend_Add.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
