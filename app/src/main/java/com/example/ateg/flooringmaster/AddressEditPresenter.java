package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

/**
 * Created by ATeg on 7/21/2017.
 */

public class AddressEditPresenter extends BasePresenter<AddressEditView> {
    private AddressClient addressClient;

    public AddressEditPresenter(AddressEditView viewInstance) {
        super(viewInstance);
        addressClient = AddressDaoSingleton.getAddressDao(null);
    }

    public void loadAddress(Integer id) {
        getView().showLoading(id);

        if (id == null || id == 0) return;

        new AsyncTask<Integer, Void, Address>() {
            @Override
            protected Address doInBackground(Integer... params) {
                Address address = AddressDaoSingleton.getAddressDao(null).get(params[0]);
                return address;
            }

            @Override
            protected void onPostExecute(Address address) {
                getView().displayAddress(address);
            }
        }.execute(id);
    }

    public void submitAddress(final Address address) {
        getView().showSubmitting();

        new AsyncTask<Address, Void, Void>() {

            @Override
            protected Void doInBackground(Address... params) {
                addressClient.update(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                getView().launchShowAddress(address.getId());
            }
        }.execute(address);
    }
}