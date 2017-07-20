package com.example.ateg.flooringmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressShowFragment extends BaseFragment<AddressShowPresenter> implements AddressShowView {
    public static final String ADDRESS_ID_TO_SHOW = "com.example.ateg.flooringmaster.AddressShowFragment.ADDRESS_ID_TO_SHOW";
    public static final String ADDRESS_TO_SHOW = "com.example.ateg.flooringmaster.AddressShowFragment.ADDRESS_TO_SHOW";
    public static final String TAG = "Address Show Fragment";

    private ProgressDialog mLoadingDialog;

    public static AddressShowFragment newInstance(Integer id) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ADDRESS_ID_TO_SHOW, id);

        // Causes network on main thread
        //arguments.putSerializable(ADDRESS_TO_SHOW, AddressDaoSingleton.getAddressDao(null).get(id));

        AddressShowFragment addressFragment = new AddressShowFragment();
        addressFragment.setArguments(arguments);
        return addressFragment;
    }

    public static AddressShowFragment newInstance(Address address) {
        Bundle arguments = new Bundle();

        if (address != null) {
            arguments.putSerializable(ADDRESS_ID_TO_SHOW, address.getId());
            arguments.putSerializable(ADDRESS_TO_SHOW, address);
        }

        AddressShowFragment addressFragment = new AddressShowFragment();
        addressFragment.setArguments(arguments);
        return addressFragment;
    }

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
        Intent intent = getActivity().getIntent();

        if (intent.hasExtra(ADDRESS_TO_SHOW)) {
            Serializable serializable = intent.getSerializableExtra(ADDRESS_TO_SHOW);
            Address address = (Address) serializable;
            mPresenter.loadAddress(address);
        } else if (intent.hasExtra(ADDRESS_ID_TO_SHOW)) {
            int id = intent.getIntExtra(ADDRESS_ID_TO_SHOW, 0);
            mPresenter.loadAddress(id);
        }
    }

    @Override
    protected void setListeners() {
        View view = getCreatedView();
        Button editButton = (Button) view.findViewById(R.id.address_show_edit_address_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Feature not implemented Yet.", Toast.LENGTH_SHORT).show();
            }
        });
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
        View view = getCreatedView();

        View loadingPanel = view.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

        TextView idTextView = (TextView) view.findViewById(R.id.address_show_id_textView);
        idTextView.setText(getString(R.string.address_id_prefix) + address.getId());

        TextView fullNameTextView = (TextView) view.findViewById(R.id.address_show_fullName_textView);
        fullNameTextView.setText(address.getFullName());

        TextView companyTextView = (TextView) view.findViewById(R.id.address_show_company_textView);
        companyTextView.setText(address.getCompany());
        if (address.getCompany() == null)
            companyTextView.setVisibility(View.GONE);

        String streetAddress = address.getStreetNumber() + " " + address.getStreetName();
        TextView streetAddressTextView = (TextView) view.findViewById(R.id.address_show_streetAddress_textView);
        streetAddressTextView.setText(streetAddress);

        String cityStateAndZip = address.getCity() + ", " + address.getState() + " " + address.getZip();
        TextView cityStateAndZipTextView = (TextView) view.findViewById(R.id.address_show_cityStateAndZip_textView);
        cityStateAndZipTextView.setText(cityStateAndZip);
    }
}
