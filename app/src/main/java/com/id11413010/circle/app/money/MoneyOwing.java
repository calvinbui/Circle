package com.id11413010.circle.app.money;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.UserDAO;
import com.id11413010.circle.app.pojo.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MoneyOwing extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_owing);
        new retrieveUsersTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.money_owing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createSpinner(List<User> names) {
        List<String> list = new ArrayList<String>();
        for(User name : names) {
            list.add(name.getFirstName());
        }
        Spinner spinner = (Spinner)findViewById(R.id.moneyOwer);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public class retrieveUsersTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            return UserDAO.retrieveAllUsers(MoneyOwing.this);
        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> list = new Gson().fromJson(json, collectionType);
            createSpinner(list);
        }
    }
}
