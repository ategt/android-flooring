package com.example.ateg.flooringmaster.errors;

/**
 * Created by ATeg on 10/11/2017.
 */

public class ValidationException extends RuntimeException {

    private ValidationErrorContainer validationErrorContainer;

    public ValidationException(String message, ValidationErrorContainer validationErrorContainer){
        super(message);
        this.validationErrorContainer = validationErrorContainer;
    }

    public ValidationErrorContainer getValidationErrorContainer(){
        return validationErrorContainer;
    }
}
