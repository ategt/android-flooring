package com.example.ateg.flooringmaster;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

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

        boolean isNetworkActive = checkForActiveNetwork();

        if (!isNetworkActive)
            return null;

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
            Log.e(TAG, "IO Problem", e);
        } finally {
            httpURLConnection.disconnect();
        }

        return null;
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
        URL url = null;
        try {
            url = new URL(uri.toString());

            HttpURLConnection httpURLConnection = getHttpURLConnection(url);

            sendData(addressJSONString, httpURLConnection, requestMethods);

            int responseCode = httpURLConnection.getResponseCode();
            Log.i(TAG, "POST Response Code :: " + responseCode);

            return new String(getBytes(httpURLConnection));
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad URL", e);
        } catch (IOException e) {
            Log.e(TAG, "IO Problem", e);
        }
        return null;
    }

    public String search(Uri uri, String searchBy, String searchText) {

//        List<Pair<String, String>> params = new ArrayList();
//        params.add(new Pair<String, String>("searchBy", ""));
//        params.add(new Pair<String, String>("searchText", ""));

        Map<String, String> param = new HashMap();

        if (searchBy != null)
            param.put("searchBy", searchBy);

        if (searchText != null)
            param.put("searchText", searchText);

        //URL siteUrl;
        HttpURLConnection conn = null;
        try {
            URL siteUrl = new URL(uri.toString());
            conn = (HttpURLConnection) siteUrl.openConnection();
            //conn.setRequestMethod("POST");
            conn.setDoInput(true);

            //conn.addRequestProperty("Accept", "application/json");

            String content = buildPostableContent(param);

            //System.out.println(content);
            Log.i(TAG, "Content: " + content);

            if (content.isEmpty()){
                Log.w(TAG, "Content empty. Skipping post.");
            } else {
//                conn.setDoOutput(true);
//                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//                out.writeBytes(content.trim());
//                out.flush();
//                out.close();

                sendData(content, conn, "POST");
            }

            int responseCode = conn.getResponseCode();
            String message = conn.getResponseMessage();

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader in = new BufferedReader(inputStreamReader);

            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
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
            if (conn != null){
                conn.disconnect();
            }
        }

        return null;
    }

    private String buildPostableContent(Map<String, String> param) {
        String content1 = "";

        Set getkey = param.keySet();
        Iterator keyIter = getkey.iterator();
        String content = "";
        for (int i = 0; keyIter.hasNext(); i++) {
            Object key = keyIter.next();
            if (i != 0) {
                content += "&";
            }
            content += key + "=" + param.get(key);
            //System.out.println("Content" + content);
        }
        return content;
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
