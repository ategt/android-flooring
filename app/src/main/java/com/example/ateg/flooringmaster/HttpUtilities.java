package com.example.ateg.flooringmaster;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.example.ateg.flooringmaster.errors.ValidationErrorContainer;
import com.example.ateg.flooringmaster.errors.ValidationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public HttpUtilities(Context context, Uri dataSourceRoot) {
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

    public String requestJSON(Uri urlUri) throws IOException {
        return requestJSON(urlUri, null);
    }

    public String requestJSON(Uri urlUri, String requestMethod) throws IOException {
        URL url = new URL(urlUri.toString());
        HttpURLConnection httpURLConnection = getHttpURLConnection(url);

        if (requestMethod != null)
            httpURLConnection.setRequestMethod(requestMethod);

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

            InputStream inputStream = httpURLConnection.getInputStream();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            return getBytes(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "IO Problem", e);
        } finally {
            httpURLConnection.disconnect();
        }

        return null;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int bytesRead = 0;
        byte[] buffer = new byte[1024];

        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    private boolean checkForActiveNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public String sendJSON(Uri uri, String addressJSONString, String requestMethods) {
        URL url;
        try {
            url = new URL(uri.toString());

            HttpURLConnection httpURLConnection = getHttpURLConnection(url);

            sendData(addressJSONString, httpURLConnection, requestMethods);

            int responseCode = httpURLConnection.getResponseCode();
            Log.i(TAG, "POST Response Code :: " + responseCode);

            InputStream errorStream = httpURLConnection.getErrorStream();
            if (errorStream != null) {
                Gson gson = new GsonBuilder().create();
                byte[] errors = getBytes(errorStream);
                String errorJson = new String(errors);
                ValidationErrorContainer validationErrorContainer
                        = gson.fromJson(errorJson, ValidationErrorContainer.class);
                Log.e(TAG, "Connection error: " + errorJson);
                throw new ValidationException("Something Must Be Wrong", validationErrorContainer);
            } else {

                byte[] bytes = getBytes(httpURLConnection);
                if (bytes == null) {
                    return null;
                } else
                    return new String(bytes);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad URL", e);
        } catch (IOException e) {
            Log.e(TAG, "IO Problem", e);
        }
        return null;
    }

    public String search(Uri uri, AddressSearchRequest addressSearchRequest) {

        Gson gson = new GsonBuilder().create();

        HttpURLConnection conn = null;
        try {
            URL siteUrl = new URL(uri.toString());
            conn = getHttpURLConnection(siteUrl);

            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            String content = gson.toJson(addressSearchRequest);

            Log.i(TAG, "Content: " + content);

            if (content.isEmpty()) {
                Log.w(TAG, "Content empty. Skipping post.");
            } else {
                sendData(content, conn, "POST");
            }

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader in = new BufferedReader(inputStreamReader);

            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();

            return sb.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad Url", e);
        } catch (ProtocolException e) {
            Log.e(TAG, "Bad Protocol", e);
        } catch (IOException e) {
            Log.e(TAG, "IO Issue", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.addRequestProperty("Content-Type", "application/json");
        httpURLConnection.addRequestProperty("Accept", "application/json");
        return httpURLConnection;
    }

    private void sendData(String addressJSONString, HttpURLConnection httpURLConnection, @javax.annotation.Nonnull String requestMethod) throws IOException {
        if (addressJSONString == null)
            return;

        // For POST only - BEGIN
        httpURLConnection.setRequestMethod(requestMethod);
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(addressJSONString.getBytes());
        os.flush();
        os.close();
        // For POST only - END
    }
}
