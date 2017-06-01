package com.example.ateg.flooringmaster;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 6/1/2017.
 */

public class AddressDaoRemoteImpl implements AddressDao {
    private final Context context;
    HttpUtilities httpUtilities = null;

    public AddressDaoRemoteImpl(Context context, HttpUtilities httpUtilities){
        this.context = context;
        this.httpUtilities = httpUtilities;
    }

    @Override
    public Address create(Address address) {
        return null;
    }

    @Override
    public void update(Address address) {

    }

    @Override
    public Address get(Integer id) {
        String addressString = null;
        try {
            addressString = httpUtilities.requestJSON(HttpUtilities.dataSourceRoot + "/address/");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(addressString);
            JSONObject dataObject = json.getJSONObject("data");
            JSONArray items = dataObject.getJSONArray("items");

//            for (int i = 0; i < items.length(); i++) {
//                JSONObject videoObject = items.getJSONObject(i);
//                Video video = new Video(videoObject.getString("title"),
//                        videoObject.getString("description"),
//                        videoObject.getJSONObject("player")
//                                .getString("default"),
//                        videoObject.getJSONObject("thumbnail")
//                                .getString("sqDefault"));
//                videos.add(video);
//            }

        } catch (JSONException e) {
            // manage exceptions
        }




        return null;
    }

    @Override
    public Address get(String input) {
        return null;
    }

    @Override
    public Address getByCompany(String company) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public List<Address> getAddressesSortedByParameter(String sortBy) {
        return null;
    }

    @Override
    public Set<String> getCompletionGuesses(String input, int limit) {
        return null;
    }

    @Override
    public List<Address> list() {
        ArrayList<Address> addresses = new ArrayList<Address>();

        String addressString = null;
        try {
            addressString = httpUtilities.requestJSON(HttpUtilities.dataSourceRoot + "/address/");
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        try {
//            JSONObject json = new JSONObject(addressString);
//            JSONObject dataObject = json.getJSONObject("data");
//            JSONArray items = dataObject.getJSONArray("items");
//
////            for (int i = 0; i < items.length(); i++) {
////                JSONObject videoObject = items.getJSONObject(i);
////                Video video = new Video(videoObject.getString("title"),
////                        videoObject.getString("description"),
////                        videoObject.getJSONObject("player")
////                                .getString("default"),
////                        videoObject.getJSONObject("thumbnail")
////                                .getString("sqDefault"));
////                videos.add(video);
////            }
//
//        } catch (JSONException e) {
//            // manage exceptions
//        }


        //String addressString = httpUtilities.requestJSON("https://mighty-eyrie-28532.herokuapp.com/address/" + 1);

        try {
            JSONObject json = new JSONObject(addressString);
            JSONObject dataObject = json.getJSONObject("data");
            JSONArray items = dataObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject addressObject = items.getJSONObject(i);
                Address address = new Address();

                address.setId(addressObject.getInt("id"));

                address.setFirstName(addressObject.getString("firstName"));
                address.setLastName(addressObject.getString("lastName"));
                address.setCompany(addressObject.getString("company"));
                address.setCity(addressObject.getString("city"));
                address.setState(addressObject.getString("state"));
                address.setStreetNumber(addressObject.getString("streetNumber"));
                address.setStreetName(addressObject.getString("streetName"));
                address.setZip(addressObject.getString("zip"));

                addresses.add(address);

//                Video video = new Video(videoObject.getString("title"),
//                        videoObject.getString("description"),
//                        videoObject.getJSONObject("player")
//                                .getString("default"),
//                        videoObject.getJSONObject("thumbnail")
//                                .getString("sqDefault"));
//                videos.add(video);


//                Video video = new Video(videoObject.getString("title"),
//                        videoObject.getString("description"),
//                        videoObject.getJSONObject("player")
//                                .getString("default"),
//                        videoObject.getJSONObject("thumbnail")
//                                .getString("sqDefault"));
//                videos.add(video);
            }

        } catch (JSONException e) {
            // manage exceptions
        }

        return addresses;
    }

    @Override
    public List<Address> list(Integer sortBy) {
        return null;
    }

    @Override
    public List<Address> searchByFirstName(String firstName) {
        return null;
    }

    @Override
    public List<Address> searchByLastName(String lastName) {
        return null;
    }

    @Override
    public List<Address> searchByCity(String city) {
        return null;
    }

    @Override
    public List<Address> searchByCompany(String company) {
        return null;
    }

    @Override
    public List<Address> searchByState(String state) {
        return null;
    }

    @Override
    public List<Address> searchByZip(String zip) {
        return null;
    }
}
