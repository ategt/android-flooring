package com.example.ateg.flooringmaster;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 6/1/2017.
 */

public class AddressDaoRemoteImpl implements AddressDao {
    private final Context context;
    HttpUtilities httpUtilities = null;
    public final String TAG = "AddressDaoRemoteImpl";

    public AddressDaoRemoteImpl(Context context, HttpUtilities httpUtilities) {
        this.context = context;
        this.httpUtilities = httpUtilities;
    }

    @Override
    public Address create(Address address) {
        Uri uri = httpUtilities.getDataSourceRoot()
                .buildUpon()
                .appendPath("address")
                .appendPath("")
                .build();

        Gson gson = new GsonBuilder().create();
        String addressJSONString = gson.toJson(address);

        String returnedString = httpUtilities.sendJSON(uri, addressJSONString, "POST");
        Address returnedAddress = gson.fromJson(returnedString, Address.class);

        return returnedAddress;
    }

    @Override
    public void update(Address address) {
        Uri uri = httpUtilities.getDataSourceRoot()
                .buildUpon()
                .appendPath("address")
                .appendPath(address.getId().toString())
                .build();

        Gson gson = new GsonBuilder().create();
        String addressJSONString = gson.toJson(address);

        httpUtilities.sendJSON(uri, addressJSONString, "PUT");
    }

    @Override
    public Address get(Integer id) {
        String addressString = null;
        try {
            addressString = httpUtilities.requestJSON(httpUtilities.getDataSourceRoot()
                    .buildUpon()
                    .appendPath("address")
                    .appendPath(id.toString())
                    .build()
                    .toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().create();
        Address address = gson.fromJson(addressString, Address.class);

        return address;
    }

    @Override
    public Address get(String input) {

        String addressString = null;
        try {
            Uri uri = httpUtilities.getDataSourceRoot()
                    .buildUpon()
                    .appendPath("address")
                    .appendPath(input)
                    .appendPath("search")
                    .build();

            addressString = httpUtilities.requestJSON(uri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().create();
        Address address = gson.fromJson(addressString, Address.class);

        return address;
    }

    @Override
    public Address getByCompany(String company) {
        return null;
    }

    @Override
    public Address delete(Integer id) {
        String addressString = null;
        try {
            Uri uri = httpUtilities.getDataSourceRoot()
                    .buildUpon()
                    .appendPath("address")
                    .appendPath(id.toString())
                    .build();

            addressString = httpUtilities.requestJSON(uri, "DELETE");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().create();
        Address address = gson.fromJson(addressString, Address.class);

        return address;
    }

    @Override
    public int size() {
        return list().size();
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
        String addressString = null;
        try {
            Uri uri = httpUtilities.getDataSourceRoot()
                    .buildUpon()
                    .appendPath("address")
                    .appendPath("")
                    .build();

            addressString = httpUtilities.requestJSON(uri.toString());
        } catch (IOException e) {
            Log.e(TAG, "IO problem.", e);
        }

        Gson gson = new GsonBuilder().create();
        Address[] addresses = gson.fromJson(addressString, Address[].class);

        Log.d(TAG, "Addresses Recieved: " + Integer.toString(addresses.length));
        return Arrays.asList(addresses);
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
        Uri uri = httpUtilities.getDataSourceRoot()
                .buildUpon()
                .appendPath("address")
                .appendPath("search")
                //.appendQueryParameter("searchBy","searchByLastName")
                //.appendQueryParameter("searchText",lastName)
                .build();

        String searchResult = httpUtilities.search(uri, "searchByLastName", lastName);


        //String jsonString = httpUtilities.sendJSON(uri, null, "POST");

        Gson gson = new GsonBuilder().create();
        Address[] addresses = gson.fromJson(searchResult, Address[].class);

        return Arrays.asList(addresses);
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
