package com.example.ateg.flooringmaster;

import com.example.ateg.flooringmaster.errors.ValidationErrorContainer;
import com.example.ateg.flooringmaster.errors.ValidationException;

/**
 * Created by ATeg on 7/21/2017.
 */

public interface AddressEditView extends LEView {
    void displayAddress(Address address);
    void showSubmitting();
    void launchShowAddress(Integer addressId);
    void displayErrors(ValidationException validationException);
}
