package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressIndexPresenter extends BasePresenter<AddressIndexView> {

    private AddressClient addressDao;
    private ResultProperties resultProperties;
    private boolean loadingNextPage;

    public AddressIndexPresenter(AddressIndexView viewInstance, AddressClient addressDao) {
        super(viewInstance);
        this.addressDao = addressDao;
        this.loadingNextPage = false;
    }

    public void loadAddresses(Integer page) {
        loadAddresses(page, 25);
    }

    public void loadAddresses(Integer page, int resultsPerPage) {
        if (page == null)
            page = 0;
        loadAddresses(new ResultProperties(AddressSortByEnum.SORT_BY_LAST_NAME, page, resultsPerPage));
    }

    public void loadNextPage() {
        if (!loadingNextPage) {
            int pageNum = resultProperties.getPageNumber() + 1;
            ResultProperties incrementedResultProperties = new ResultProperties(resultProperties.getSortByEnum(),
                    pageNum,
                    resultProperties.getResultsPerPage());

            loadAddresses(incrementedResultProperties);
        }
    }

    public void loadAddresses(ResultProperties resultProperties) {
        this.resultProperties = resultProperties;
        if (loadingNextPage) return;
        loadingNextPage = true;

        new AsyncTask<ResultProperties, Void, List<Address>>() {

            @Override
            protected List<Address> doInBackground(ResultProperties... params) {
                return addressDao.list(params[0]);
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                loadingNextPage = false;
                if (addresses != null) {
                    getView().appendAddresses(addresses);
                } else {
                    getView().showError(new IllegalArgumentException("Invalid Address"));
                }
            }
        }.execute(resultProperties);
    }
}