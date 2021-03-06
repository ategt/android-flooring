package com.example.ateg.flooringmaster;

import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 6/1/2017.
 */


public interface AddressDao extends AddressClient {

    public Address create(Address address);
    public void update(Address address);
    public Address get(Integer id);
    public Address get(String input);
    public Address getByCompany(String company);
    public Address delete(Integer id);

    public int size();
    public int size(boolean block);
    public int size(AddressSearchRequest addressSearchRequest);
    public int size(boolean block, AddressSearchRequest addressSearchRequest);

    public List<Address> getAddressesSortedByParameter(String sortBy);
    public List<String> getCompletionGuesses(String input, int limit);
    @Deprecated
    public List<Address> list();

    public List<Address> list(ResultSegment<AddressSortByEnum> resultProperties);
    public List<Address> searchByFirstName(String firstName);
    public List<Address> searchByLastName(String lastName);
    public List<Address> searchByCity(String city);
    public List<Address> searchByCompany(String company);
    public List<Address> searchByState(String state);
    public List<Address> searchByZip(String zip);
}
