package com.example.ateg.flooringmaster;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.ateg.flooringmaster.errors.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressIndexPresenter extends BasePresenter<AddressIndexView> {

    private AddressClient addressDao;
    //private ResultProperties resultProperties;
    private boolean loadingNextPage;
    private List<AddressAppendListener> addressAppendListeners;

    //private AddressSearchRequest addressSearchRequest;

    public final int DEFAULT_ITEMS_TO_LOAD = 50;

    public AddressIndexPresenter(AddressIndexView viewInstance, AddressClient addressDao) {
        super(viewInstance);
        this.addressDao = addressDao;
        this.loadingNextPage = false;
        addressAppendListeners = new ArrayList<>();
    }

    public void loadAddresses(Integer page) {
        loadAddresses(page, DEFAULT_ITEMS_TO_LOAD);
    }

    public void loadAddresses(Integer page, int resultsPerPage) {
        if (page == null)
            page = 0;
        loadAddresses(new AddressResultSegment(AddressSortByEnum.SORT_BY_LAST_NAME, page, resultsPerPage));
    }

    public void loadNextPage() {
        if (!loadingNextPage) {
            int pageNum = AddressResultSegmentSingleton.getAddressResultSegment().getPageNumber() + 1;
            AddressResultSegment incrementedResultProperties = new AddressResultSegment(AddressResultSegmentSingleton.getAddressResultSegment().getSortByEnum(),
                    pageNum,
                    AddressResultSegmentSingleton.getAddressResultSegment().getResultsPerPage());

            loadAddresses(incrementedResultProperties);
        }
    }

    public void loadAddresses(AddressResultSegment resultProperties) {
        AddressResultSegmentSingleton.setAddressResultSegment(resultProperties);
        //AddressDataListSingleton.setResultProperties(resultProperties);

        if (loadingNextPage) return;
        loadingNextPage = true;

        final AddressSearchRequest tempAddressSearchRequest = getAddressSearchRequest();

        AsyncTask<AddressResultSegment, Void, List<Address>> asyncTask;

        if (tempAddressSearchRequest != null) {
            asyncTask = searchAsyncTask(tempAddressSearchRequest);
        } else {
            asyncTask = listAsyncTask();
        }

        registerNetworkCall(asyncTask);
        asyncTask.execute(resultProperties);
    }

    @NonNull
    private AsyncTask<AddressResultSegment, Void, List<Address>> listAsyncTask() {
        return new AsyncTask<AddressResultSegment, Void, List<Address>>() {

            ValidationException validationException;

            @Override
            protected List<Address> doInBackground(AddressResultSegment... params) {
                try {
                    return addressDao.list(params[0]);
                } catch (ValidationException validationException) {
                    this.validationException = validationException;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                addressesRecieved(addresses, validationException);
            }
        };
    }

    @NonNull
    private AsyncTask<AddressResultSegment, Void, List<Address>> searchAsyncTask(final AddressSearchRequest tempAddressSearchRequest) {
        return new AsyncTask<AddressResultSegment, Void, List<Address>>() {

            ValidationException validationException;

            @Override
            protected List<Address> doInBackground(AddressResultSegment... params) {
                try {
                    return addressDao.search(tempAddressSearchRequest, params[0]);
                } catch (ValidationException validationException) {
                    this.validationException = validationException;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                addressesRecieved(addresses, validationException);
            }
        };
    }

    private void addressesRecieved(List<Address> addresses, ValidationException validationException) {
        if (!addresses.isEmpty())
            loadingNextPage = false;

        if (addresses != null) {
            getView().appendAddresses(addresses);
            notifyAddressesAppended();
        } else if (validationException != null) {
            getView().showError(validationException);
        } else {
            getView().showError(new IllegalArgumentException("Invalid Address"));
        }
    }

    private void notifyAddressesAppended() {
        for (AddressAppendListener addressAppendListener : addressAppendListeners) {
            addressAppendListener.onAddressesAppended();
        }

        addressAppendListeners.clear();
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
        return AddressSearchRequestSingleton.getAddressSearchRequest();
        //return addressSearchRequest;
    }

    public void setAddressSearchRequest(AddressSearchRequest addressSearchRequest) {
        AddressSearchRequest oldAddressSearchRequest = getAddressSearchRequest();

        boolean hasAddressRequestChanged = !Objects.equals(oldAddressSearchRequest, addressSearchRequest);
        AddressSearchRequestSingleton.setAddressSearchRequest(addressSearchRequest);

        if (hasAddressRequestChanged) {
            AddressDataListSingleton.clear();
            loadingNextPage = false;
            getView().resetList();
        }

        //AddressDataListSingleton.setAddressSearchRequest(addressSearchRequest);
        //this.addressSearchRequest = addressSearchRequest;
    }

    public void clearSearch() {
        setAddressSearchRequest(null);

    }

    public void initialAddressLoad() {
        AddressDataListSingleton.clear();
        this.loadAddresses(AddressResultSegmentSingleton.getDefaultAddressResultSegment(DEFAULT_ITEMS_TO_LOAD));
    }

    public void addAddressesAppenededListener(AddressAppendListener addressAppendListener) {
        addressAppendListeners.add(addressAppendListener);
    }
}