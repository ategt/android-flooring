package com.example.ateg.flooringmaster;

import android.view.View;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressCreateFragment extends BaseFragment<AddressCreatePresenter> {
    @Override
    protected int layout() {
        return R.layout.fragment_edit_address;
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
    protected AddressCreatePresenter createPresenter() {
        return new AddressCreatePresenter(this);
    }
}
