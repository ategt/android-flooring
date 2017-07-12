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

    //private Address address;
    private ProgressDialog mLoadingDialog;
    //private AddressClient addressClient;
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

        addressClient = new AddressClient(getActivity(),
                addressDao,
                ACTION_FOR_INTENT_CALLBACK);

        Integer id = (Integer) getArguments().getSerializable(EXTRA_ADDRESS_ID);
        //address = null;
        getContent(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private void getContent(Integer id) {
        addressClient.get(id);
        mLoadingDialog = ProgressDialog.show(getActivity(), "Getting Data ...", "Waiting For Results...", true);
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflator.inflate(R.layout.fragment_address, parent, false);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            Activity activity = getActivity();
            if (activity != null) {
                ActionBar actionBar = activity.getActionBar();
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        return view;
    }

    @Override
    protected int layout() {
        return 0;
    }

    @Override
    protected void setUi(View v) {

    }

    @Override
    protected void init() {

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // clear the progress indicator
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }

            address = (Address) intent.getSerializableExtra(AddressClient.ADDRESS_SERIALIZABLE_EXTRA);

            //updateAddress();
            Toast.makeText(context, "Address: " + address.getFullName(), Toast.LENGTH_SHORT).show();
        }
    };

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
        mLoadingDialog.show();
        mLoadingDialog = ProgressDialog.show(getActivity(), "Getting Data ...", "Waiting For Results...", true);
    }

    @Override
    public void showError(Throwable e) {
        mLoadingDialog.dismiss();
        //showToast(e.getMessage());
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAddress(Address address) {
        mLoadingDialog.dismiss();
        // Populate view with user data
    }
}