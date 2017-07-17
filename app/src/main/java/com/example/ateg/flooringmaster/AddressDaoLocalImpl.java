package com.example.ateg.flooringmaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 7/11/2017.
 */

public abstract class AddressDaoLocalImpl implements AddressDao {

    private static List<Address> addresses;
    private static AddressDaoLocalImpl addressDaoLocal;

    private AddressDaoLocalImpl(List<Address> addresses){
        if (this.addresses == null){
            this.addresses = addresses;
        } else {
            this.addresses.addAll(addresses);
        }
    }

    public static AddressDaoLocalImpl getLocalDao(){
        if (addressDaoLocal == null){
            addressDaoLocal = new AddressDaoLocalImpl(new ArrayList()) {
                @Override
                public List<Address> list(ResultProperties resultProperties) {
                    return null;
                }
            };
        }
        return addressDaoLocal;
    }

    public static AddressDaoLocalImpl getLocalDao(List<Address> addresses){
        if (addressDaoLocal == null){
            addressDaoLocal = new AddressDaoLocalImpl(addresses) {
                @Override
                public List<Address> list(ResultProperties resultProperties) {
                    return null;
                }
            };
        } else {
            AddressDaoLocalImpl.addresses.addAll(addresses);
        }

        return addressDaoLocal;
    }

    @Override
    public Address create(Address address) {
        addresses.add(address);
        return address;
    }

    @Override
    public void update(Address address) {
        Integer id = address.getId();
        Address foundAddress = get(id);

        addresses.remove(foundAddress);
        addresses.add(address);
    }

    @Override
    public Address get(Integer id) {
        for (Address testAddress : addresses){
            if (testAddress != null && testAddress.getId() == id){
                return testAddress;
            }
        }
        return null;
    }

    @Override
    public Address get(String input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Address getByCompany(String company) {
        return null;
    }

    @Override
    public Address delete(Integer id) {
        Address address = get(id);
        addresses.remove(address);
        return address;
    }

    @Override
    public int size() {
        return addresses.size();
    }

    @Override
    public List<Address> getAddressesSortedByParameter(String sortBy) {
        return null;
    }

    @Override
    public Set<String> getCompletionGuesses(String input, int limit) {
        return null;
    }

    @Override
    public List<Address> list() {
        return addresses;
    }

    @Override
    public List<Address> list(Integer sortBy) {
        return null;
    }

    @Override
    public List<Address> searchByFirstName(String firstName) {
        return null;
    }

    @Override
    public List<Address> searchByLastName(String lastName) {
        return null;
    }

    @Override
    public List<Address> searchByCity(String city) {
        return null;
    }

    @Override
    public List<Address> searchByCompany(String company) {
        return null;
    }

    @Override
    public List<Address> searchByState(String state) {
        return null;
    }

    @Override
    public List<Address> searchByZip(String zip) {
        return null;
    }
}