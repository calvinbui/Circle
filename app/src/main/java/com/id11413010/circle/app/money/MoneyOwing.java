package com.id11413010.circle.app.money;

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
        listView = (ListView)findViewById(R.id.moneyList);
        // initialise arrayList
        arrayList = new ArrayList<Money>();
        // initialise our array adapter with references to this activity, the list activity and array list
        adapter = new MoneyAdapter(this, R.layout.listmoney, arrayList);
        // set the adapter for the list
        listView.setAdapter(adapter);
        new RetrieveMoneyTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Money item = (Money)adapterView.getItemAtPosition(i);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MoneyOwing.this);
                builder.setTitle(Double.toString(item.getAmount()))
                .setMessage(item.getDescription())
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(R.string.removeOwing, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                        if (sp.getInt(Constants.USERID, 0) == item.getTo()) {
                            new DeleteMoneyTask(item, i).execute();
                            dialogInterface.cancel();
                            Toast.makeText(getApplicationContext(), getString(R.string.moneyDeleted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.moneyNotUser), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            startActivity(new Intent(this, MoneyOwingAdd.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class RetrieveMoneyTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            return MoneyDAO.retrieveOwing(MoneyOwing.this);
        }

        @Override
        protected void onPostExecute(String json) {
            Type collectionType = new TypeToken<ArrayList<Money>>(){}.getType();
            List<Money> list = new Gson().fromJson(json, collectionType);
            for (Money m : list)
                arrayList.add(m);
            adapter.notifyDataSetChanged();
        }
    }

    public class DeleteMoneyTask extends AsyncTask<Void, Void, Void> {
        private Money money;
        private int position;

        private DeleteMoneyTask(Money money, int position) {
            this.money = money;
            this.position = position;
        }

        protected Void doInBackground(Void... params) {
            MoneyDAO.deleteOwing(money);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.remove(money);
            adapter.notifyDataSetChanged();
        }
    }
}
