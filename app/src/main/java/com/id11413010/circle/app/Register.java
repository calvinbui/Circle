package com.id11413010.circle.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Register extends Activity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = (EditText)findViewById(R.id.firstNameRegister_et);
        lastName = (EditText)findViewById(R.id.lastNameRegister_et);
        email = (EditText)findViewById(R.id.emailRegister_et);
        password = (EditText)findViewById(R.id.passwordRegister_et);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    public void registerUser(View v) {
        if (checkText()) {
            try {

                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://calvinbui.no-ip.biz/android/register.php?");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("firstName", firstNameString));
                nameValuePairs.add(new BasicNameValuePair("lastName",lastNameString));
                nameValuePairs.add(new BasicNameValuePair("email",emailString));
                nameValuePairs.add(new BasicNameValuePair("password",passwordString));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);

                startActivity(new Intent(this, Login.class));
            } catch (ClientProtocolException e) {
            e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
            }
        }
    }

    private boolean checkText() {
        if (firstName.getText().toString().equals("")) {
            makeToast(getText(R.string.firstNameMissing).toString());
            return false;
        }
        else if (lastName.getText().toString().equals("")) {
            makeToast(getText(R.string.lastNameMissing).toString());
            return false;
        }
        else if (email.getText().toString().equals("")) {
            makeToast(getText(R.string.emailMissing).toString());
            return false;
        }
        else if (password.getText().toString().equals("")) {
            makeToast(getText(R.string.passwordMissing).toString());
            return false;
        }
        return true;
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private class RegisterTask extends AsyncTask<Void, Void, Void> {

        String firstNameString;
        String lastNameString;
        String emailString;
        String passwordString;

        protected void onPreExecute() {
            firstNameString = firstName.getText().toString();
            lastNameString = lastName.getText().toString();
            emailString = email.getText().toString();
            passwordString = password.getText().toString();
        }

        protected void doInBackground(Void... params) {
            //here you execute the post request
            // You can call postData() here
        }

        protected void onPostExecute() {
            //onPostExecute is called when background task executed
            //this is the place to update the interface

        }
    }
}
