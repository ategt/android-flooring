package com.example.ateg.flooringmaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressMVPListFragment extends ListBaseFragment<AddressListMVPPresenter> implements AddressListView {

    private static final String TAG = "AddressListMvpActivity";

    @Override
    public void onResume() {
        super.onResume();
        ((AddressMVPListFragment.AddressAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.address_list);

        AddressMVPListFragment.AddressAdapter addressAdapter
                = new AddressMVPListFragment.AddressAdapter(getActivity(), 0, AddressDataListSingleton.getAddressDao(getActivity()));
        setListAdapter(addressAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AddressMVPListFragment.AddressAdapter addressAdapter = (AddressMVPListFragment.AddressAdapter) getListAdapter();
        Address selectedAddress = addressAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), AddressPagerActivity.class);
        intent.putExtra(AddressFragment.EXTRA_ADDRESS, selectedAddress);
        intent.putExtra(AddressFragment.EXTRA_ADDRESS_ID, selectedAddress.getId());

        startActivity(intent);
    }

    @Override
    public void showError(Throwable ex) {
        Log.e(TAG, "Thing wrong.", ex);
    }

    @Override
    public void showLoading(Integer id) {

    }

    @Override
    public void appendAddresses(List<Address> addressList) {
        AddressAdapter listAdapter = (AddressAdapter) getListAdapter();
        if (listAdapter == null) {
        } else {
            AddressDataListSingleton.getAddressDao(getActivity()).addAll(addressList);
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int layout() {
        return R.layout.list_addresses;
    }

    @Override
    protected void setUi(View v) {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void populate() {
        mPresenter.loadAddresses(new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 0 , 25));
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected AddressListMVPPresenter createPresenter() {
        return new AddressListMVPPresenter(this, AddressDaoSingleton.getAddressDao(getActivity()));
    }

    private class AddressAdapter extends ArrayAdapter<Address> {
        public AddressAdapter(Context context, int resource, List<Address> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if a view was not input inflate one.
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_address, null);
            }

            Address address = getItem(position);

            TextView nameTextView =
                    (TextView) convertView.findViewById(R.id.address_list_item_nameTextView);
            nameTextView.setText(address.getLastName() + ", " + address.getFirstName());
            TextView companyTextView =
                    (TextView) convertView.findViewById(R.id.address_list_item_companyTextView);
            companyTextView.setText(address.getCompany());

            return convertView;
        }
    }
}