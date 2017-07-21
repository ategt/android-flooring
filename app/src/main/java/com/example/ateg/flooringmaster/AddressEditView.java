package com.example.ateg.flooringmaster;

/**
 * Created by ATeg on 7/21/2017.
 */

public interface AddressEditView extends LEView {
    void displayAddress(Address address);
    void showSubmitting();
    void launchShowAddress(Integer addressId);
}
