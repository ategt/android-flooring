package com.example.ateg.flooringmaster;

import java.util.List;
import java.util.Set;

/**
 * Created by ATeg on 6/1/2017.
 */


public interface AddressClient {

    public Address create(Address address);
    public void update(Address address);
    public Address get(Integer id);
    public Address get(String input);
    public Address delete(Integer id);

    public int size();
    public int size(boolean block);

    public List<String> getCompletionGuesses(String input, int limit);
    public List<Address> list(ResultSegment<AddressSortByEnum> resultProperties);
    public List<Address> search(AddressSearchRequest addressSearchRequest, ResultSegment<AddressSortByEnum> resultProperties);}
