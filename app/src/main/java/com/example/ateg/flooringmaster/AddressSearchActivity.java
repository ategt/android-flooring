package com.example.ateg.flooringmaster;

import android.app.Fragment;

/**
 * Created by ATeg on 10/12/2017.
 */

public class AddressSearchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddressSearchFragment();
    }
}
