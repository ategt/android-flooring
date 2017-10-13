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

        AsyncTask<AddressSearchRequest, Void, List<Address>> asyncTask =
                new AsyncTask<AddressSearchRequest, Void, List<Address>>() {

                    ValidationException validationException;

                    @Override
                    protected List<Address> doInBackground(AddressSearchRequest... params) {
                        try {
                            return addressDao.search(params[0],
                                    new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 0, Integer.MAX_VALUE));
                        } catch (ValidationException validationException) {
                            this.validationException = validationException;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(List<Address> addresses) {
                        if (addresses != null)
                            getView().searchResults(addresses);
                        else
                            getView().showError(validationException);
                    }
                };
    }
}