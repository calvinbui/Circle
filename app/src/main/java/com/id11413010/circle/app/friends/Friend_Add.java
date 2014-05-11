package com.id11413010.circle.app.friends;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.id11413010.circle.app.R;

public class Friend_Add extends Activity {

    private Button addFriend;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        addFriend = (Button)findViewById(R.id.addFriendBtn);
        firstNameEditText = (EditText)findViewById(R.id.addFriendFirstName);
        lastNameEditText = (EditText)findViewById(R.id.addFriendLastName);
    }

    public void createFriend(View v) {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();

        if(firstName.trim().equals("") || lastName.trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.emptyName, Toast.LENGTH_SHORT).show();
        }
        else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.returnToFriend) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
