package com.example.ateg.flooringmaster;

import com.example.ateg.flooringmaster.errors.ValidationErrorContainer;

/**
 * Created by ATeg on 7/21/2017.
 */

public interface AddressEditView extends LEView {
    void displayAddress(Address address);
    void showSubmitting();
    void launchShowAddress(Integer addressId);
    void displayErrors(ValidationErrorContainer validationErrorContainer);
}
