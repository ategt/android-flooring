package com.example.ateg.flooringmaster;

import com.example.ateg.flooringmaster.errors.ValidationException;

import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public interface AddressCreateView extends LEView {
    void addressCreationSuccessful(Address address);
    void showSubmitting();
    void showValidationError(ValidationException ex);
}
