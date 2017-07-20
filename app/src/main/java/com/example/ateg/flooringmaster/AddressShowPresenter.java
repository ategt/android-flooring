package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressShowPresenter extends BasePresenter<AddressShowView> {

    private AddressShowView mAddressShowView;
    private AddressDao addressClient;

    public AddressShowPresenter(AddressShowView viewInstance) {
        super(viewInstance);
        mAddressShowView = viewInstance;
        addressClient = AddressDaoSingleton.getAddressDao(null);
    }

    public void loadAddress(Address address) {
        mAddressShowView.displayAddress(address);
    }

    public void loadAddress(Integer id) {


        if (id == null || id == 0)
            return;

        new AsyncTask<Integer, Void, Address>() {
            @Override
            protected Address doInBackground(Integer... params) {
                Address address = addressClient.get(params[0]);
                return address;
            }

            @Override
            protected void onPostExecute(Address address) {
                loadAddress(address);
            }
        }.execute(id);
    }
}