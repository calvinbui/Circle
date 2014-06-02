package com.id11413010.circle.app.moneyOwing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
 * Created by Calvin on 2/06/2014.
 */
public class MoneyOwingPaid extends Activity {
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
        // retrieve all past payments
        new RetrievePaidMoneyTask().execute();

        // set an onclick listener onto each row of the list which shows a popup dialog
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the at the position clicked
                final Money item = (Money)adapterView.getItemAtPosition(i);
                // create a new alert dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(MoneyOwingPaid.this);
                // set the title
                builder.setTitle(getText(R.string.dollarSign).toString() + Double.toString(item.getAmount()))
                        // set the message of the dialog
                        .setMessage(item.getDescription())
                                // set a button to cancel the dialog
                        .setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                // set a button to set the payment to paid
                // show the alert dialog
                builder.show();
            }
        });

        Log.i(Constants.LOG, "Started Money Owing Paid");
    }

    /**
     * An AsyncTask which captures the information inputted by the User and sends it via Internet
     * to the a web service to be added into the database. Separates network activity from the main
     * thread. Responsible for retrieving paid payments from the database.
     */
    public class RetrievePaidMoneyTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            // retrieve past payments from the database
            return MoneyDAO.retrievePaid(MoneyOwingPaid.this);
        }

        @Override
        protected void onPostExecute(String json) {
            // create a new list of past payments objects from the json String
            Type collectionType = new TypeToken<ArrayList<Money>>(){}.getType();
            List<Money> list = new Gson().fromJson(json, collectionType);
            // add each payment from the list into the array list
            for (Money m : list)
                arrayList.add(m);
            // notify the adapter that the underlying data has changed to update its view.
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MoneyOwing.class));
        finish();
    }
}
