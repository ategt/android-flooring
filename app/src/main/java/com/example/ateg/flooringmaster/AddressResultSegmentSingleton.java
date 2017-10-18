package com.example.ateg.flooringmaster;

/**
 * Created by ATeg on 10/18/2017.
 */

public class AddressResultSegmentSingleton {
    private static AddressResultSegment addressResultSegment;

    public static AddressResultSegment getAddressResultSegment(){
        return addressResultSegment;
    }

    public static void setAddressResultSegment(AddressResultSegment addressResultSegment) {
        AddressResultSegmentSingleton.addressResultSegment = addressResultSegment;
    }

    public static AddressResultSegment getDefaultAddressResultSegment(int defaultItemsToLoad) {
        return new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, 0, defaultItemsToLoad);
    }
}
