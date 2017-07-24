package com.example.ateg.flooringmaster;

import android.content.Context;
import android.net.Uri;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ATeg on 7/12/2017.
 */

@Module
public class AddressDaoSingleton {
    private static AddressDao addressDao;

    private AddressDaoSingleton(Context context) {
        Uri baseUri = Uri.parse(context.getString(R.string.starting_root_url));
        AddressDao tempAddressDao = new AddressClientImpl(context, new HttpUtilities(context, baseUri));
        addressDao = new AddressBufferedClient(tempAddressDao, 20);
    }

    public static AddressDao getAddressDao(Context context) {
        if (addressDao == null)
            new AddressDaoSingleton(context);
        return addressDao;
    }

    public static void setAddressDao(AddressDao addressDao){
        AddressDaoSingleton.addressDao = addressDao;
    }
}
