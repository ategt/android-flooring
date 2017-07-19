package com.example.ateg.flooringmaster;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressShowFragment extends BaseFragment<AddressShowPresenter> implements AddressShowView {
    public static final String ADDRESS_ID_TO_SHOW = "com.example.ateg.flooringmaster.AddressShowFragment.ADDRESS_ID_TO_SHOW";
    public static final String TAG = "Address Show Fragment";

    private ProgressDialog mLoadingDialog;

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
        return new AddressShowPresenter(this);
    }

    @Override
    public void showError(Throwable ex) {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
        Log.e(TAG, "Error occured.", ex);
        Toast.makeText(getActivity(), "Error Ocurred!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showLoading(Integer id) {

    }

    @Override
    public void displayAddress(Address address) {

    }
}
