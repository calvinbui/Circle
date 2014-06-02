package com.id11413010.circle.app.network;

import android.util.Log;

import com.id11413010.circle.app.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * This class provides network/internet methods responsible for connecting outside of the application.
 *
 */
public class Network {
    /**
     * The URL for the web service.
     */
    //private static String Url = "http://calvinbui.no-ip.biz/android/";
    private static String Url = "http://192.168.43.63/android/";

    /**
     * Connection to the database through a HTTP Client.
     * Performs a HTTP POST containing a List of name value pairs (containing a key and value).
     * Returns a String of containing the web service's response.
     */
    public static String httpConnection(String request, List<NameValuePair> nameValuePairs) {
        // create a new HTTP client
        HttpClient httpclient = new DefaultHttpClient();
        // create a new http post to the specified URL
        HttpPost httppost = new HttpPost(Url + request);
        // initialise a String to hold the HTML response
        String htmlResponse = new String();
        try {
            // post the List to the URL
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // store the response from the web service
            Log.i(Constants.LOG, "Starting network call");
            HttpResponse response = httpclient.execute(httppost);
            // convert the HTML response to an entity, removing the HTTP and networking sections
            Log.i(Constants.LOG, "Network response successful");
            HttpEntity entity = response.getEntity();
            // convert the response to a String
            htmlResponse = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            Log.e(Constants.LOG, "Network connection failure - ClientProtocolException");
        } catch (IOException e) {
            Log.e(Constants.LOG, "Network connection failure - IOException");
        }
        // return the String
        return htmlResponse;
    }
}
