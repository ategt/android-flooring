package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressIndexPresenter extends BasePresenter<AddressIndexView> {

    private AddressDao addressDao;
    private ResultProperties resultProperties;

    public AddressIndexPresenter(AddressIndexView viewInstance, AddressDao addressDao) {
        super(viewInstance);
        this.addressDao = addressDao;
    }

    public void loadAddresses(Integer page) {
        loadAddresses(page, 25);
    }

    public void loadAddresses(Integer page, int resultsPerPage) {
        if (page == null)
            page = 0;
        loadAddresses(new ResultProperties(AddressSortByEnum.SORT_BY_LAST_NAME, page,resultsPerPage));
    }

    public void loadNextPage(){
        int pageNum = resultProperties.getPageNumber() + 1;
        ResultProperties incrementedResultProperties = new ResultProperties(resultProperties.getSortByEnum(),
                pageNum,
                resultProperties.getResultsPerPage());

        loadAddresses(incrementedResultProperties);
    }

    public void loadAddresses(ResultProperties resultProperties) {
        this.resultProperties = resultProperties;

        new AsyncTask<ResultProperties, Void, List<Address>>() {

            @Override
            protected List<Address> doInBackground(ResultProperties... params) {
                return addressDao.list(params[0]);
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (addresses != null){
                    getView().appendAddresses(addresses);
                } else {
                    getView().showError(new IllegalArgumentException("Invalid Address"));
                }
            }
        }.execute(resultProperties);
    }
}