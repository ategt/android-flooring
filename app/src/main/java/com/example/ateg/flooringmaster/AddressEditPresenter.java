package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

import com.example.ateg.flooringmaster.errors.ValidationError;
import com.example.ateg.flooringmaster.errors.ValidationErrorContainer;
import com.example.ateg.flooringmaster.errors.ValidationException;

import java.util.List;

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

        new AsyncTask<Address, Void, Address>() {

            ValidationErrorContainer validationErrorContainer;

            @Override
            protected Address doInBackground(Address... params) {
                try {
                    addressClient.update(params[0]);
                    return params[0];
                } catch (ValidationException ex) {
                    validationErrorContainer = ex.getValidationErrorContainer();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Address address) {
                if (validationErrorContainer != null) {
                    getView().displayErrors(validationErrorContainer);
                } else
                    getView().launchShowAddress(address.getId());
            }
        }.execute(address);
    }
}