package com.example.ateg.flooringmaster;

/**
 * Created by ATeg on 10/18/2017.
 */

public class AddressSearchRequestSingleton {

    private static AddressSearchRequest addressSearchRequest;

    public static AddressSearchRequest getAddressSearchRequest() {
//        if (addressSearchRequest == null) {
//            addressSearchRequest = getDefaultAddressSearchRequest();
//        }
        return addressSearchRequest;
    }

    public static void setAddressSearchRequest(AddressSearchRequest addressSearchRequest) {
        AddressSearchRequestSingleton.addressSearchRequest = addressSearchRequest;
    }

    public static AddressSearchRequest getDefaultAddressSearchRequest() {
        return new AddressSearchRequest();
    }
}
