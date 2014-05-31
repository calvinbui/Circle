package com.id11413010.circle.app.moneyOwing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;
import com.id11413010.circle.app.dao.MoneyDAO;
import com.id11413010.circle.app.pojo.Money;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for listing the outstanding payments users within a circle owe
 * one another.
 */
public class MoneyOwing extends Activity {
    /**
     * ListView that will hold our items references back to main.xml
     */
    private ListView listView;
    /**
     * Array Adapter that will hold our ArrayList and display the items on the ListView
     */
    private MoneyAdapter adapter;
    /**
     * List that will host our items and allow us to modify the UserAdapter
     */
    private ArrayList<Money> arrayList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_owing_home);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView)findViewById(R.id.moneyList);
        // initialise arrayList
        arrayList = new ArrayList<Money>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new MoneyAdapter(this, R.layout.listmoney, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        // retrieve all oustanding payments
        new RetrieveMoneyTask().execute();

        // set an onclick listener onto each row of the list which shows a popup dialog
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the at the position clicked
                final Money item = (Money)adapterView.getItemAtPosition(i);
                // create a new alert dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(MoneyOwing.this);
                // set the title
                builder.setTitle(Double.toString(item.getAmount()))
                // set the message of the dialog
                .setMessage(item.getDescription())
                // set a button to cancel the dialog
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                // set a button to set the payment to paid
                .setPositiveButton(R.string.removeOwing, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // get the user's id to ensure they are the lender
                        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                        if (sp.getInt(Constants.USERID, 0) == item.getTo()) {
                            // update the payment as paid
                            new DeleteMoneyTask(item).execute();
                            // close the dialog
                            dialogInterface.cancel();
                            Toast.makeText(getApplicationContext(), getString(R.string.moneyDeleted), Toast.LENGTH_SHORT).show();
                        } else {
                            // if they are not the lender, toast the user that they cnanot delete the payment
                            Toast.makeText(getApplicationContext(), getString(R.string.moneyNotUser), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                // show the alert dialog
                builder.show();
            }
        });
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
        if (id == R.id.createMoneyOwing) {
            // start an activity to add a new money owing
            startActivity(new Intent(this, MoneyOwingAdd.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for outstanding payments from the database.
     */
    public class RetrieveMoneyTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            // retrieve outstanding payments from the database
            return MoneyDAO.retrieveOwing(MoneyOwing.this);
        }

        @Override
        protected void onPostExecute(String json) {
            // create a new list of oustanding payments objects from the json String
            Type collectionType = new TypeToken<ArrayList<Money>>(){}.getType();
            List<Money> list = new Gson().fromJson(json, collectionType);
            // add each payment from the list into the array list
            for (Money m : list)
                arrayList.add(m);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }

    public class DeleteMoneyTask extends AsyncTask<Void, Void, Void> {
        /**
         * A money object to delete
         */
        private Money money;

        private DeleteMoneyTask(Money money) {
            this.money = money;
        }

        protected Void doInBackground(Void... params) {
            // delete the money object
            MoneyDAO.deleteOwing(money);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // remove the object from the arraylist
            adapter.remove(money);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }
}
