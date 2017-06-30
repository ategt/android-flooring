package com.example.ateg.flooringmaster;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import static java.net.Proxy.Type.HTTP;

/**
 * Created by ATeg on 6/30/2017.
 */

public class RestClient
{

//    public static String makeRequest(String path, Map params)
//            throws Exception {
//
//        DefaultHttpClient httpclient = new DefaultHttpClient();
//        HttpPost httpost = new HttpPost(path);
//        Iterator iter = params.entrySet().iterator();
//
//        JSONObject holder = new JSONObject();
//
//        while(iter.hasNext()) {
//            Map.Entry pairs = (Map.Entry)iter.next();
//            String key = (String)pairs.getKey();
//            Map m = (Map)pairs.getValue();
//
//            JSONObject data = new JSONObject();
//            Iterator iter2 = m.entrySet().iterator();
//            while(iter2.hasNext()) {
//                Map.Entry pairs2 = (Map.Entry)iter2.next();
//                data.put((String)pairs2.getKey(), (String)pairs2.getValue());
//            }
//            holder.put(key, data);
//        }
//
//        StringEntity se = new StringEntity(holder.toString());
//        httpost.setEntity(se);
//        httpost.setHeader("Accept", "application/json");
//        httpost.setHeader("Content-type", "application/json");
//
//        ResponseHandler responseHandler = new BasicResponseHandler();
//        response = httpclient.execute(httpost, responseHandler);
//    }

}