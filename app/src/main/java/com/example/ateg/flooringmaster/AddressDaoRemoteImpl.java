package com.example.ateg.flooringmaster;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 6/1/2017.
 */

public class AddressDaoRemoteImpl implements AddressDao {

    HttpUtilities httpUtilities = null;
    public final String TAG = "AddressDaoRemoteImpl";
    Gson gson;

    public AddressDaoRemoteImpl(Context context, HttpUtilities httpUtilities) {
        this.httpUtilities = httpUtilities;
        gson = new GsonBuilder().create();
    }

    @Override
    public Address create(Address address) {
        Uri uri = httpUtilities.getDataSourceRoot()
                .buildUpon()
                .appendPath("address")
                .appendPath("")
                .build();

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
                .appendPath("")
                .build();

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

        Address address = gson.fromJson(addressString, Address.class);

        return address;
    }

    @Override
    public Address getByCompany(String company) {
        return get(company);
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

        Address address = gson.fromJson(addressString, Address.class);

        return address;
    }

    @Override
    public int size() {
        return list().size();
    }

    @Override
    public int size(boolean block) {
        return size();
    }

    @Override
    public List<Address> getAddressesSortedByParameter(String sortBy) {
        Integer sortByInt = AddressSortByEnum.parse(sortBy).intValue();

        return list(sortByInt);
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

        Address[] addresses = gson.fromJson(addressString, Address[].class);

        Log.d(TAG, "Addresses Recieved: " + Integer.toString(addresses.length));
        return Arrays.asList(addresses);
    }

    @Override
    public List<Address> list(ResultProperties resultProperties) {

        String addressString = null;
        try {
            Uri uri = httpUtilities.getDataSourceRoot()
                    .buildUpon()
                    .appendPath("address")
                    .appendPath("")
                    .appendQueryParameter("page", resultProperties.getPageNumber() == null ? "0" : resultProperties.getPageNumber().toString())
                    .appendQueryParameter("results", resultProperties.getResultsPerPage() == null ? Integer.toString(Integer.MAX_VALUE) : resultProperties.getResultsPerPage().toString())
                    .appendQueryParameter("sort_by", resultProperties.getSortByEnum() == null ? "" : resultProperties.getSortByEnum().value())
                    .build();

            addressString = httpUtilities.requestJSON(uri.toString());
        } catch (IOException e) {
            Log.e(TAG, "IO problem.", e);
        }

        Address[] addresses = gson.fromJson(addressString, Address[].class);

        Log.d(TAG, "Addresses Recieved: " + Integer.toString(addresses.length));
        return Arrays.asList(addresses);
    }

    @Override
    public List<Address> search(AddressSearchRequest addressSearchRequest, ResultProperties resultProperties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Address> list(Integer sortBy) {
        return list(new ResultProperties(AddressSortByEnum.parse(sortBy), null, null));
    }

    @Override
    public List<Address> searchByFirstName(String input) {
        return searchClient(input, AddressSearchByOptionEnum.FIRST_NAME);
    }

    @Override
    public List<Address> searchByLastName(String lastName) {
        return searchClient(lastName, AddressSearchByOptionEnum.LAST_NAME);
    }

    private List<Address> searchClient(String lastName, AddressSearchByOptionEnum addressSearchByOptionEnum) {
        Uri uri = httpUtilities.getDataSourceRoot()
                .buildUpon()
                .appendPath("address")
                .appendPath("search")
                .build();

        String searchResult = httpUtilities.search(uri, new AddressSearchRequest(lastName, addressSearchByOptionEnum));

        if (searchResult == null)
            return null;

        Address[] addresses = gson.fromJson(searchResult, Address[].class);

        return Arrays.asList(addresses);
    }

    @Override
    public List<Address> searchByCity(String input) {
        return searchClient(input, AddressSearchByOptionEnum.CITY);
    }

    @Override
    public List<Address> searchByCompany(String input) {
        return searchClient(input, AddressSearchByOptionEnum.COMPANY);
    }

    @Override
    public List<Address> searchByState(String input) {
        return searchClient(input, AddressSearchByOptionEnum.STATE);

    }

    @Override
    public List<Address> searchByZip(String input) {
        return searchClient(input, AddressSearchByOptionEnum.ZIP);

    }
}
