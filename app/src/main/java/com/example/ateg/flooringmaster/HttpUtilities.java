package com.example.ateg.flooringmaster;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ATeg on 6/1/2017.
 */

public class HttpUtilities {

    private final Context context;
    public final String TAG = "HttpUtilities";

    //public static final String dataSourceRoot = "https://mighty-eyrie-28532.herokuapp.com";
    //public static final String dataSourceRoot = "http://127.0.0.1:8080";
    //public static final String dataSourceRoot = "http://192.168.128.251:8080";
    private Uri dataSourceRoot;

    public HttpUtilities(Context context, Uri dataSourceRoot ){
        this.context = context;
        this.setDataSourceRoot(dataSourceRoot);
    }

    public Uri getDataSourceRoot() {
        return dataSourceRoot;
    }

    public void setDataSourceRoot(Uri dataSourceRoot) {
        this.dataSourceRoot = dataSourceRoot;
    }

    public JSONObject requestJSONObject(String urlSpec) throws IOException, JSONException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.addRequestProperty("Accept", "application/json");

        String jsonString = new String(getBytes(httpURLConnection));
        return new JSONObject(jsonString);
    }

    public JSONArray requestJSONArray(String urlSpec) throws IOException, JSONException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.addRequestProperty("Accept", "application/json");

        String jsonString = new String(getBytes(httpURLConnection));
        return new JSONArray(jsonString);
    }

    public String requestJSON(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.addRequestProperty("Accept", "application/json");

        String jsonString = new String(getBytes(httpURLConnection));
        return jsonString;
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        return getBytes(httpURLConnection);
    }

    private byte[] getBytes(HttpURLConnection httpURLConnection) {
        try {
            Log.d(TAG, httpURLConnection.getURL().toString());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = httpURLConnection.getInputStream();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e(TAG, e.getMessage());
            Log.e(TAG, "IO Problem");
        } finally {
            httpURLConnection.disconnect();
        }

        return null;
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
