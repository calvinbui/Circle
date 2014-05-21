package com.id11413010.circle.app;

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
    private static HttpPost httppost;
    private static HttpClient httpclient;
    private static String Url = "http://calvinbui.no-ip.biz/android/";
    private static HttpEntity entity;
    private static String htmlResponse;
    private static HttpResponse response;


    public static String httpConnection(String request, List<NameValuePair> nameValuePairs) {
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost(Url + request);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            entity = response.getEntity();
            htmlResponse = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlResponse;
    }
}
