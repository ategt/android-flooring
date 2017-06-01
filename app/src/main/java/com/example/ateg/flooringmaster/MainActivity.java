package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    AddressDao addressDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.addressDao = new AddressDaoRemoteImpl(this, new HttpUtilities(this));
    }

    public void ATask(){
        new AsyncTask<String, Void, List<Address>>() {
            @Override
            protected void onPreExecute() {
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
            protected void onPostExecute(List<Address> res) {
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
}
