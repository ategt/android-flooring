package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressListMVPActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddressMVPListFragment();
    }
}
