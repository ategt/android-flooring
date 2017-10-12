package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

import com.example.ateg.flooringmaster.errors.ValidationException;

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

    public void createAddress(Address address) {
        mAddressCreateView.showSubmitting();

        new AsyncTask<Address, Void, Address>() {

            ValidationException validationException = null;

            @Override
            protected Address doInBackground(Address... params) {
                try {
                    return addressClient.create(params[0]);
                } catch (ValidationException validationException) {
                    this.validationException = validationException;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Address address) {
                if (address != null)
                    mAddressCreateView.addressCreationSuccessful(address);
                else if (validationException != null)
                        //mAddressCreateView.
                    mAddressCreateView.showError(null);
            }
        }.execute(address);
    }
}
