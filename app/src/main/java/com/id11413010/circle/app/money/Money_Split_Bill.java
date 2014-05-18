package com.id11413010.circle.app.money;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Money_Split_Bill extends Activity implements NumberPicker.OnValueChangeListener{

    private NumberPicker peopleCount;
    private int count;
    private EditText totalPrice;
    private TextView splitPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money__split__bill);
        peopleCount = (NumberPicker)findViewById(R.id.numOfPeople_np);
        totalPrice = (EditText)findViewById(R.id.totalPrice_et);
        splitPrice = (TextView)findViewById(R.id.splitPrice_tv);
        new getCircleMembersCount().execute();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculate();
            }
        };
        totalPrice.addTextChangedListener(textWatcher);
        peopleCount.setOnValueChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.money__split__bill, menu);
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

    private void calculate() {
        Double total, people;
        DecimalFormat df = new DecimalFormat("#.00");
        people = (double) peopleCount.getValue();
        if(totalPrice.getText().toString().matches("")) {
            total = 0.0;
        }
        else {
            total = Double.valueOf(totalPrice.getText().toString());
        }
        splitPrice.setText(getText(R.string.dollarSign).toString() + df.format(total / people));
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i2) {
        calculate();
    }

    private class getCircleMembersCount extends AsyncTask<Void, Void, Void> {
        private String s;

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            String circle = sp.getString(Constants.CIRCLE, null);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://calvinbui.no-ip.biz/android/circle_member_count.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("circle", circle));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                s = EntityUtils.toString(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            count = Integer.valueOf(s);
            peopleCount.setMinValue(1);
            peopleCount.setMaxValue(count);
            peopleCount.setWrapSelectorWheel(false);
            peopleCount.setValue(1);
        }
    }
}
