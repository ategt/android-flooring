package com.example.ateg.flooringmaster;

import android.app.Application;
import android.os.AsyncTask;

/**
 * Presenter for user view
 */
public class AddressPresenter extends BasePresenter<AddressView> {

    private AddressDao addressDao;

    AddressPresenter(AddressView view, AddressDao addressDao) {
        super(view);
        this.addressDao = addressDao;
    }

    public void loadAddress(int addressId) {

        getView().showLoading();

        new AsyncTask<Integer, Void, Address>(){

            @Override
            protected Address doInBackground(Integer... params) {
                return addressDao.get(params[0]);
            }

            @Override
            protected void onPostExecute(Address address) {
                super.onPostExecute(address);

                if (address != null){
                    getView().setAddress(address);
                } else {
                    getView().showError(new IllegalArgumentException("Invalid Address"));
                }
            }
        }.execute(addressId);
    }
}