package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressCreatePresenter extends BasePresenter<AddressCreateView> {
    private AddressCreateView mAddressCreateView;
    private AddressDao addressClient;

    public AddressCreatePresenter(AddressCreateView viewInstance) {
        super(viewInstance);
        mAddressCreateView = viewInstance;
        addressClient = AddressDaoSingleton.getAddressDao(null);
    }

    public void createAddress(final Address address) {
        mAddressCreateView.showSubmitting();


        new AsyncTask<Address, Void, Address>() {

            @Override
            protected Address doInBackground(Address... params) {
                return addressClient.create(address);
            }

            @Override
            protected void onPostExecute(Address address) {
                if (address != null)
                    mAddressCreateView.addressCreationSuccessful(address);
                else
                    mAddressCreateView.showError(null);
            }
        }.execute(address);
    }
}
