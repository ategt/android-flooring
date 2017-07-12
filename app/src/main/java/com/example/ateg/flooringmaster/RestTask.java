package com.example.ateg.flooringmaster;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by ATeg on 7/11/2017.
 */
public abstract class RestTask extends AsyncTask<Void, Void, String>
{
//    private static final String TAG = "AsyncRestTask";
//    public static final String HTTP_RESPONSE = "httpResponse";
//
//    private Context mContext;
//    private HttpClient mClient;
//    private String mAction;
//
//    public RestTask(Context context, String action) {
//        mContext = context;
//        mAction = action;
//        mClient = new DefaultHttpClient();
//    }
//
//    public RestTask(Context context, String action, HttpClient client) {
//        mContext = context;
//        mAction = action;
//        mClient = client;
//    }
//
//    @Override
//    protected String doInBackground(HttpUriRequest... params) {
//        try {
//            HttpUriRequest request = params[0];
//            HttpResponse serverResponse = mClient.execute(request);
//            BasicResponseHandler handler = new BasicResponseHandler();
//            return handler.handleResponse(serverResponse);
//        }
//        catch (Exception e) {
//            // TODO handle this properly
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        Log.i(TAG, "RESULT = " + result);
//        Intent intent = new Intent(mAction);
//        intent.putExtra(HTTP_RESPONSE, result);
//
//        // broadcast the completion
//        mContext.sendBroadcast(intent);
//    }

}