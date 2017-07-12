package com.example.ateg.flooringmaster;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by ATeg on 7/10/2017.
 */

public class AddressFragment extends BaseFragment<AddressPresenter> implements AddressView {

    public static final String EXTRA_ADDRESS_ID = "com.example.ateg.flooringmaster.address_id";
    public static final String EXTRA_ADDRESS = "com.example.ateg.flooringmaster.address";
    private static final String TAG = "Address Fragment";
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_COMMUNICATE";

    private ProgressDialog mLoadingDialog;
    private AddressDao addressDao;
    private Integer id;

    public static AddressFragment newInstance(Integer id) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(EXTRA_ADDRESS_ID, id);

        AddressFragment addressFragment = new AddressFragment();
        addressFragment.setArguments(arguments);
        return addressFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
        Uri baseUri = Uri.parse(getString(R.string.starting_root_url));
        addressDao = new AddressDaoRemoteImpl(getActivity(), new HttpUtilities(getActivity(), baseUri));

        id = (Integer) getArguments().getSerializable(EXTRA_ADDRESS_ID);
    }

    @Override
    protected int layout() {
        return R.layout.fragment_address;
    }

    @Override
    protected void setUi(View v) {
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            Activity activity = getActivity();
            if (activity != null) {
                ActionBar actionBar = activity.getActionBar();
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void init() {

    }

    @Override
    protected void populate() {
        mPresenter.loadAddress(id);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected AddressPresenter createPresenter() {
        return new AddressPresenter(this, addressDao);
    }

    @Override
    public void showLoading() {
        mLoadingDialog = ProgressDialog.show(getActivity(), "Getting Data ...", "Waiting For Results...", true);
    }

    @Override
    public void showError(Throwable e) {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();

        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAddress(Address address) {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
        // Populate view with user data
    }
}