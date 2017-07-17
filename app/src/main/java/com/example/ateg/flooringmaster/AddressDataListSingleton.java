package com.example.ateg.flooringmaster;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressDataListSingleton {
    private static List<Address> addressList;

    public static List<Address> getAddressDao(Context context) {
        if (addressList == null)
            addressList = new ArrayList();
        return addressList;
    }
}
