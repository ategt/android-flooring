package com.example.ateg.flooringmaster;

import android.app.Fragment;

/**
 * Created by ATeg on 7/21/2017.
 */

public class AddressEditActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AddressEditFragment();
    }
}
