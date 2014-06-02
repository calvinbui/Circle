package com.id11413010.circle.app.moneyOwing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.home.HomeScreen;
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                // only pick one item from the list
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                // set the selected item as checked
                listView.setItemChecked(i, true);
                //final Money longItem = (Money)adapterView.getItemAtPosition(i);

                startActionMode(new ActionMode.Callback() {
                    // Called when the action mode is created; startActionMode() was called
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        // Inflate a menu resource providing context menu items
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.action_mode_delete, menu);
                        return true;
                    }

                    // Called each time the action mode is shown. Always called after onCreateActionMode, but
                    // may be called multiple times if the mode is invalidated.
                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false; // Return false if nothing is done
                    }

                    // Called when the user selects a contextual menu item
                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteListObject:
                                SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                                int pos = listView.getCheckedItemPosition();
                                Money m = (Money)listView.getItemAtPosition(pos);
                                if (sp.getInt(Constants.USERID, 0) == m.getTo()) {
                                    // update the payment as paid
                                    new DeleteMoneyTask(m).execute();
                                    Toast.makeText(getApplicationContext(), getString(R.string.moneyDeleted), Toast.LENGTH_SHORT).show();
                                } else {
                                    // if they are not the lender, toast the user that they cannot delete the payment
                                    Toast.makeText(getApplicationContext(), getString(R.string.moneyNotUser), Toast.LENGTH_SHORT).show();
                                }
                                mode.finish(); // Action picked, so close the CAB
                                return true;
                            default:
                                return false;
                        }
                    }

                    // Called when the user exits the action mode
                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        listView.clearChoices();
                        //workaround for some items not being unchecked.
                        //see http://stackoverflow.com/a/10542628/1366471
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            (listView.getChildAt(i).getBackground()).setState(new int[] { 0 });
                        }
                        listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    }
                });
                return true;
            }
        });
        Log.i(Constants.LOG, "Started Money Owing");
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
            // create a new list of outstanding payments objects from the json String
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }
}
