package com.example.ateg.flooringmaster;

import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public interface AddressIndexView extends LEView {
    void appendAddresses(List<Address> addressList);
    void scrollToAddress(Address address);

    void resetList();

    void showDeleted(Address address);
}
