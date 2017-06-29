package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    AddressDao addressDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.addressDao = new AddressDaoRemoteImpl(this, new HttpUtilities(this, Uri.parse(getString(R.string.starting_root_url))));
    }

    public void aTask(){
        new AsyncTask<String, Void, List<Address>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                mDialog = ProgressDialog.show(MainActivity.this,
//                        "Load in progress", "Wait ...", true, true);

            }

            @Override
            protected List<Address> doInBackground(String... params) {
                //String realUrl = URL.replace("$QUERY$", "Android");
                //return requestContent(realUrl);

                return addressDao.list();
            }

            @Override
            protected void onPostExecute(List<Address> result) {

                super.onPostExecute(result);

                for(Address address : result) {
                    DisplayAddress(address);
                }

//                videos = new ArrayList<MediaStore.Video>();
//
//                try {
//                    JSONObject json = new JSONObject(res);
//                    JSONObject dataObject = json.getJSONObject("data");
//                    JSONArray items = dataObject.getJSONArray("items");
//
//                    for (int i = 0; i < items.length(); i++) {
//                        JSONObject videoObject = items.getJSONObject(i);
//                        Video video = new Video(videoObject.getString("title"),
//                                videoObject.getString("description"),
//                                videoObject.getJSONObject("player")
//                                        .getString("default"),
//                                videoObject.getJSONObject("thumbnail")
//                                        .getString("sqDefault"));
//
//                        videos.add(video);
//                    }
//
//                } catch (JSONException e) {
//                    // manage exceptions
//                }
//
//                mVideosAdapter = new VideosListAdapter(MainActivity.this, videos);
//                mVideosLv.setAdapter(mVideosAdapter);
//                // dismiss progress dialog
//                Utils.dismissDialog(mDialog);
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // we target SDK > API 11
    }


    @Override
    protected void onStart() {
        super.onStart();
        //progressDialog = ProgressDialog.show(this, "Please Wait", "Progress...", true);
        //CountDownTimer timer = new CountDownTimer(3000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                progressDialog.dismiss();
//                doDatabaseThings();
//
//            }
//        }.start();

        aTask();
    }


    public void DisplayAddress(Address cursor){
        Toast.makeText(this, "id: " + cursor.getId() + "\n"
                + "Name: " + cursor.getLastName() + ", " + cursor.getFirstName() + "\n"
                + "Company: " + cursor.getCompany(), Toast.LENGTH_LONG).show();
    }
}
