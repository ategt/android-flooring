package com.example.ateg.flooringmaster;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressDataListSingleton {
    private static List<Address> addressList;

    public static List<Address> getDataList(Context context) {
        return respondWithAddressList();
    }

    public static List<Address> getDataList() {
        return respondWithAddressList();
    }

    @NonNull
    private static List<Address> respondWithAddressList() {
        if (addressList == null)
            addressList = new ArrayList();
        return addressList;
    }

    public static Address getOrNull(Integer position){

        if (position < respondWithAddressList().size()) {
            return respondWithAddressList().get(position);
        } else {
            return null;
        }
    }

    public static int size(){
        return respondWithAddressList().size();
    }

    public static int indexOf(Address address){
        return respondWithAddressList().indexOf(address);
    }

    public static void appendAll(Collection<? extends Address> addressCollection){
        respondWithAddressList().addAll(addressCollection);
    }
}