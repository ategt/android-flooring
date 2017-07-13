package com.example.ateg.flooringmaster;

import android.util.LruCache;

import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 7/12/2017.
 */

public class AddressDaoBufferedRemoteImp implements AddressDao {

    private AddressDao addressDao;
    private LruCache<Integer, Address> lruCache;

    public AddressDaoBufferedRemoteImp(AddressDao addressDao, int maxSize) {
        if (addressDao == null || addressDao.getClass().isInstance(AddressDaoBufferedRemoteImp.class))
            throw new UnsupportedOperationException("The input to AddressDaoBufferedRemoteImpl is unsupported.");
        this.addressDao = addressDao;
        lruCache = new LruCache<>(maxSize);
    }

    @Override
    public Address create(Address address) {
        Address returnedAddress = addressDao.create(address);

        if (returnedAddress != null)
            lruCache.put(returnedAddress.getId(), returnedAddress);

        return returnedAddress;
    }

    @Override
    public void update(Address address) {
        if (address != null) {
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

        lruCache.put(address.getId(), address);
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

        return returnedAddress;
    }

    @Override
    public int size() {
        return addressDao.size();
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
