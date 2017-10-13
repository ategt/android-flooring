package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

import com.example.ateg.flooringmaster.errors.ValidationException;

import java.util.List;

/**
 * Created by ATeg on 10/12/2017.
 */

public class AddressSearchPresenter extends BasePresenter<AddressSearchView> {

    private AddressClient addressDao;

    public AddressSearchPresenter(AddressSearchView viewInstance, AddressDao addressDao) {
        super(viewInstance);
        this.addressDao = addressDao;
    }

    public void launchSearch(String query, AddressSearchByOptionEnum searchByOptionEnum) {
        AddressSearchRequest addressSearchRequest = new AddressSearchRequest(query, searchByOptionEnum);

        getView().launchSearch(addressSearchRequest);
    }
}