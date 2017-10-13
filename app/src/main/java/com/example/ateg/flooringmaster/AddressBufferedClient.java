package com.example.ateg.flooringmaster;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ATeg on 7/12/2017.
 */

public class AddressBufferedClient implements AddressDao, AddressClient {

    private final static String TAG = "AddressBufferedClient";
    private AddressDao addressDao;
    private LruCache<Integer, Address> lruCache;
    private Pair<Long, Integer> sizePair;
    private boolean updateInProgress;
    private AsyncTask<Void, Void, Integer> sizeUpdater;

    public AddressBufferedClient(AddressDao addressDao, int maxSize) {
        if (addressDao == null || addressDao.getClass().isInstance(AddressBufferedClient.class))
            throw new UnsupportedOperationException("The input to AddressDaoBufferedRemoteImpl is unsupported.");
        this.addressDao = addressDao;
        lruCache = new LruCache(maxSize);
        updateSize(false);
        updateInProgress = false;

        size(false);
    }

    @Override
    public Address create(Address address) {
        Address returnedAddress = addressDao.create(address);

        if (returnedAddress != null)
            lruCache.put(returnedAddress.getId(), returnedAddress);

        updateSize(true);
        return returnedAddress;
    }

    @Override
    public void update(Address address) {
        if (address != null) {
            if (address.getId() == null){
                Log.e(TAG, "An address was passed to the update method with no ID");
                throw new UnsupportedOperationException("An address was passed to the update method with no ID");
            }
            lruCache.remove(address.getId());
            lruCache.put(address.getId(), address);
        }

        addressDao.update(address);
    }

    @Override
    public Address get(Integer id) {
        Address address = lruCache.get(id);

        if (address != null)
            return address;

        address = addressDao.get(id);

        if (address == null)
            return null;

        lruCache.put(address.getId(), address);
        updateSize(false);
        return address;
    }

    @Override
    public Address get(String input) {
        return addressDao.get(input);
    }

    @Override
    public Address getByCompany(String company) {
        return addressDao.getByCompany(company);
    }

    @Override
    public Address delete(Integer id) {
        Address returnedAddress = addressDao.delete(id);

        if (returnedAddress != null)
            lruCache.remove(returnedAddress.getId());

        updateSize(true);
        return returnedAddress;
    }

    private void updateSize(boolean forceUpdate) {
        if (forceUpdate || sizePair == null || sizePair.second == null || sizePair.first + 60000 < System.currentTimeMillis()) {

            updateInProgress = true;

            if (sizeUpdater != null){
                sizeUpdater.cancel(true);
            }

            sizeUpdater = new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return addressDao.size();
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    sizePair = new Pair(System.currentTimeMillis(), integer);
                    updateInProgress = false;
                    sizeUpdater = null;
                }
            };
            sizeUpdater.execute();
        }
    }

    @Override
    public int size() {
        return size(false);
    }

    @Override
    public int size(boolean blockForUpdate) {
        updateSize(false);

        if (blockForUpdate) {
            try {
                while (updateInProgress) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
             Log.e(TAG, "Problem in size buffering.", e);
                e.printStackTrace();
            }
        }

        if (sizePair == null)
            return 0;
        else
            return sizePair.second;
    }

    @Override
    public List<Address> getAddressesSortedByParameter(String sortBy) {
        return addressDao.getAddressesSortedByParameter(sortBy);
    }

    @Override
    public Set<String> getCompletionGuesses(String input, int limit) {
        return addressDao.getCompletionGuesses(input, limit);
    }

    @Override
    public List<Address> list() {
        return addressDao.list();
    }

    @Override
    public List<Address> list(Integer sortBy) {
        return addressDao.list(sortBy);
    }

    @Override
    public List<Address> list(ResultProperties resultProperties) {
        List<Address> addressList = addressDao.list(resultProperties);
        for (Address address : addressList) {
            lruCache.put(address.getId(), address);
        }
        return addressList;
    }

    @Override
    public List<Address> search(AddressSearchRequest addressSearchRequest, ResultProperties resultProperties) {
        return addressDao.search(addressSearchRequest, resultProperties);
    }

    @Override
    public List<Address> searchByFirstName(String firstName) {
        return addressDao.searchByFirstName(firstName);
    }

    @Override
    public List<Address> searchByLastName(String lastName) {
        return addressDao.searchByLastName(lastName);
    }

    @Override
    public List<Address> searchByCity(String city) {
        return addressDao.searchByCity(city);
    }

    @Override
    public List<Address> searchByCompany(String company) {
        return addressDao.searchByCompany(company);
    }

    @Override
    public List<Address> searchByState(String state) {
        return addressDao.searchByState(state);
    }

    @Override
    public List<Address> searchByZip(String zip) {
        return addressDao.searchByZip(zip);
    }
}
