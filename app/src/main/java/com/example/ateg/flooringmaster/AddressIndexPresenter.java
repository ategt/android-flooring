package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

import com.example.ateg.flooringmaster.errors.ValidationException;

import java.util.List;
import java.util.Objects;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressIndexPresenter extends BasePresenter<AddressIndexView> {

    private AddressClient addressDao;
    //private ResultProperties resultProperties;
    private boolean loadingNextPage;

    //private AddressSearchRequest addressSearchRequest;

    public final int DEFAULT_ITEMS_TO_LOAD = 50;

    public AddressIndexPresenter(AddressIndexView viewInstance, AddressClient addressDao) {
        super(viewInstance);
        this.addressDao = addressDao;
        this.loadingNextPage = false;
    }

    public void loadAddresses(Integer page) {
        loadAddresses(page, DEFAULT_ITEMS_TO_LOAD);
    }

    public void loadAddresses(Integer page, int resultsPerPage) {
        if (page == null)
            page = 0;
        loadAddresses(new ResultProperties(AddressSortByEnum.SORT_BY_LAST_NAME, page, resultsPerPage));
    }

    public void loadNextPage() {
        if (!loadingNextPage) {
            int pageNum = AddressDataListSingleton.getResultProperties().getPageNumber() + 1;
            ResultProperties incrementedResultProperties = new ResultProperties( AddressDataListSingleton.getResultProperties().getSortByEnum(),
                    pageNum,
                    AddressDataListSingleton.getResultProperties().getResultsPerPage());

            loadAddresses(incrementedResultProperties);
        }
    }

    public void loadAddresses(ResultProperties resultProperties) {
        AddressDataListSingleton.setResultProperties(resultProperties);

        if (loadingNextPage) return;
        loadingNextPage = true;

        AsyncTask<ResultProperties, Void, List<Address>> asyncTask = new AsyncTask<ResultProperties, Void, List<Address>>() {

            AddressSearchRequest tempAddressSearchRequest = getAddressSearchRequest();
            ValidationException validationException;

            @Override
            protected List<Address> doInBackground(ResultProperties... params) {
                try {
                    if (tempAddressSearchRequest != null) {
                        return addressDao.search(tempAddressSearchRequest, params[0]);
                    } else
                        return addressDao.list(params[0]);
                } catch (ValidationException validationException){
                    this.validationException = validationException;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (!addresses.isEmpty())
                    loadingNextPage = false;

                if (addresses != null) {
                    getView().appendAddresses(addresses);
                } else if(validationException != null){
                    getView().showError(validationException);
                }else {
                    getView().showError(new IllegalArgumentException("Invalid Address"));
                }
            }
        };
        registerNetworkCall(asyncTask);
        asyncTask.execute(resultProperties);
    }

    public boolean considerLoadingNextPage(int positionInList, int listSize) {
        return considerLoadingNextPage(positionInList, listSize, (DEFAULT_ITEMS_TO_LOAD / 2));
    }

    public boolean considerLoadingNextPage(int positionInList, int listSize, int itemOffset) {
        if (positionInList + itemOffset > listSize) {
            loadNextPage();
            return true;
        } else {
            return false;
        }
    }

    public void scrollToId(Integer id) {
        if (id == null) {
            return;
        }

        AsyncTask<Integer, Void, Address> asyncTask = new AsyncTask<Integer, Void, Address>() {

            ValidationException validationException;

            @Override
            protected Address doInBackground(Integer... params) {
                try {
                    return addressDao.get(params[0]);
                } catch (ValidationException validationException) {
                    this.validationException = validationException;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Address address) {
                if (address != null) {
                    getView().scrollToAddress(address);
                } else if (validationException != null) {
                    getView().showError(validationException);
                } else {
                    getView().showError(new IllegalArgumentException("Invalid Address"));
                }
            }
        };
        registerNetworkCall(asyncTask);
        asyncTask.execute(id);
    }

    private AddressSearchRequest getAddressSearchRequest() {
        return AddressDataListSingleton.getAddressSearchRequest();
        //return addressSearchRequest;
    }

    public void setAddressSearchRequest(AddressSearchRequest addressSearchRequest) {
        if (!Objects.equals(getAddressSearchRequest(), addressSearchRequest)){
            AddressDataListSingleton.clear();
            loadingNextPage = false;
            getView().resetList();
        }

        AddressDataListSingleton.setAddressSearchRequest(addressSearchRequest);
        //this.addressSearchRequest = addressSearchRequest;
    }

    public void clearSearch(){
        setAddressSearchRequest(null);

    }
}