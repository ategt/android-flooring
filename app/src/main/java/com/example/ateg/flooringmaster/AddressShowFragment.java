package com.example.ateg.flooringmaster;

import android.view.View;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressShowFragment extends BaseFragment<AddressShowPresenter> {
    public static final String ADDRESS_ID_TO_SHOW = "com.example.ateg.flooringmaster.AddressShowFragment.ADDRESS_ID_TO_SHOW";

    @Override
    protected int layout() {
        return R.layout.fragment_show_address;
    }

    @Override
    protected void setUi(View v) {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void populate() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected AddressShowPresenter createPresenter() {
        return null;
    }
}
