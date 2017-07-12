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

public class AddressFragment extends Fragment {

    public static final String EXTRA_ADDRESS_ID = "com.example.ateg.flooringmaster.address_id";
    public static final String EXTRA_ADDRESS = "com.example.ateg.flooringmaster.address";
    private static final String TAG = "Address Fragment";
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_COMMUNICATE";

    private Address address;
    private ProgressDialog progress;
    private AddressClient addressClient;

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
        addressClient = new AddressClient(getActivity(),
                new AddressDaoRemoteImpl(getActivity(), new HttpUtilities(getActivity(), baseUri)),
                ACTION_FOR_INTENT_CALLBACK);

        Integer id = (Integer) getArguments().getSerializable(EXTRA_ADDRESS_ID);
        address = null;
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
        progress = ProgressDialog.show(getActivity(), "Getting Data ...", "Waiting For Results...", true);
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // clear the progress indicator
            if (progress != null) {
                progress.dismiss();
            }

            address = (Address) intent.getSerializableExtra(AddressClient.ADDRESS_SERIALIZABLE_EXTRA);

            //updateAddress();
            Toast.makeText(context, "Address: " + address.getFullName(), Toast.LENGTH_SHORT).show();
        }
    };
}