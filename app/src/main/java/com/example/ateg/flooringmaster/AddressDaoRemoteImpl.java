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
import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 6/1/2017.
 */

public class AddressDaoRemoteImpl implements AddressDao {
    private final Context context;
    HttpUtilities httpUtilities = null;
    public final String TAG = "AddressDaoRemoteImpl";

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
            //addressString = httpUtilities.requestJSON(httpUtilities.getDataSourceRoot().buildUpon().appendPath("address") + "/address/");
            addressString = httpUtilities.requestJSON(httpUtilities.getDataSourceRoot()
                    .buildUpon()
                    .appendPath("address")
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
            String path = httpUtilities.getDataSourceRoot().getPath();
            Uri uri = httpUtilities.getDataSourceRoot();

            String host = uri.getHost();
            String path2 = uri.getPath();
            int port = uri.getPort();
            String auth = uri.getAuthority();
            String eAuth = uri.getEncodedAuthority();
            String frag = uri.getEncodedFragment();
            String ePath = uri.getEncodedPath();
            String eQuery = uri.getEncodedQuery();
            String fragment = uri.getFragment();
            String query = uri.getQuery();
            String scheme = uri.getScheme();
            String userInfo = uri.getUserInfo();
            String uriString = uri.toString();

            //HttpUtilities.getDataSourceRoot().get

            addressString = httpUtilities.requestJSON(httpUtilities.getDataSourceRoot().toString() + "/address/");
        } catch (IOException e) {
            Log.e(TAG, "IO problem.", e);
        }

        try {
            JSONArray jsonArray = new JSONArray(addressString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject addressObject = jsonArray.getJSONObject(i);
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
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSON problems.", e);
        }

        Log.d(TAG, "Addresses Recieved: " + Integer.toString(addresses.size()));
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
