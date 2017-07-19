package com.example.ateg.flooringmaster;

import android.app.Fragment;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressCreateActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AddressCreateFragment();
    }
}
