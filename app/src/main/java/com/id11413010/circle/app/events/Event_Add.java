package com.id11413010.circle.app.events;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Event_Add extends Activity {

    private TextView startDate, endDate, startTime, endTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText name, location, details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__add);
        name = (EditText)findViewById(R.id.eventName_et);
        location = (EditText)findViewById(R.id.eventLocation_et);
        details = (EditText)findViewById(R.id.eventDescription_et);
        startDate = (TextView)findViewById(R.id.eventStartDate_tv);
        endDate =(TextView)findViewById(R.id.eventEndDate_tv);
        startTime = (TextView)findViewById(R.id.eventStartTime_tv);
        endTime = (TextView)findViewById(R.id.eventEndTime_tv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event__add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.createEvent) {
            new createEventTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickEndDate(View v) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                    // Display Selected date in textbox
                    endDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void pickStartDate(View v) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Display Selected date in textbox
                    startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void pickStartTime(View v) {
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                    // Display Selected time in textbox
                    startTime.setText(hourOfDay + ":" + minute);
                }
            }, mHour, mMinute, false);
        tpd.show();
    }

    public void pickEndTime(View v) {
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                    // Display Selected time in textbox
                    endTime.setText(hourOfDay + ":" + minute);
                }
            }, mHour, mMinute, false);
        tpd.show();
    }

    private class createEventTask extends AsyncTask<Void, Void, Void> {

        private String nameString;
        private String locationString;
        private String detailsString;
        private String startDateString;
        private String startTimeString;
        private String endDateString;
        private String endTimeString;
        private String circle;

        protected void onPreExecute() {
            String SDateString = startDate.getText().toString();
            String EDateString = endDate.getText().toString();

            try {
                startDateString = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MM-yyyy").parse(SDateString));
                endDateString = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MM-yyyy").parse(EDateString));
            } catch (Exception e) {
                e.printStackTrace();
            }


            nameString = name.getText().toString();
            locationString = location.getText().toString();
            detailsString = details.getText().toString();
            startTimeString = startTime.getText().toString();
            endTimeString = endTime.getText().toString();

            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            circle = sp.getString(Constants.CIRCLE, null);
        }

        protected Void doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://calvinbui.no-ip.biz/android/create_event.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("name", nameString));
            nameValuePairs.add(new BasicNameValuePair("description",detailsString));
            nameValuePairs.add(new BasicNameValuePair("location",locationString));
            nameValuePairs.add(new BasicNameValuePair("startDate",startDateString));
            nameValuePairs.add(new BasicNameValuePair("endDate",endDateString));
            nameValuePairs.add(new BasicNameValuePair("startTime",startTimeString));
            nameValuePairs.add(new BasicNameValuePair("endTime",endTimeString));
            nameValuePairs.add(new BasicNameValuePair("circle", circle));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            startActivity(new Intent(Event_Add.this, Event.class));
        }
    }
}
