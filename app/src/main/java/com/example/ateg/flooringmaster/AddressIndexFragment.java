package com.example.ateg.flooringmaster;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressIndexFragment extends BaseFragment<AddressIndexPresenter> implements AddressIndexView {

    private static final String TAG = "AddressListMvpActivity";

    @Override
    public void onResume() {
        super.onResume();
        //((AddressIndexFragment.AddressAdapter) getListAdapter()).notifyDataSetChanged();
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
        ListView listView = (ListView) getCreatedView().findViewById(R.id.address_index_listView);

        ListAdapter listAdapter = listView.getAdapter();
        AddressAdapter addressAdapter = (AddressAdapter) listAdapter;

        if (listAdapter == null) {
        } else {
            AddressDataListSingleton.getAddressDao(getActivity()).addAll(addressList);
            addressAdapter.notifyDataSetChanged();
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
        getActivity().setTitle(R.string.address_list);

        AddressIndexFragment.AddressAdapter addressAdapter
                = new AddressIndexFragment.AddressAdapter(getActivity(), 0, AddressDataListSingleton.getAddressDao(getActivity()));

        ListView listView = (ListView) getCreatedView().findViewById(R.id.address_index_listView);
        listView.setAdapter(addressAdapter);
    }

    @Override
    protected void populate() {
        mPresenter.loadAddresses(new ResultProperties(AddressSortByEnum.SORT_BY_COMPANY, 0, 25));
    }

    @Override
    protected void setListeners() {
        // I had a scroll change listener here.
        //Log.i(TAG, "Scroll state changed");

        View createdView = getCreatedView();

        ListView listView = (ListView) createdView.findViewById(R.id.address_index_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address address = (Address) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), AddressShowActivity.class);
                intent.putExtra(AddressShowFragment.ADDRESS_TO_SHOW, address);
                intent.putExtra(AddressShowFragment.ADDRESS_ID_TO_SHOW, address.getId());

                startActivity(intent);
            }
        });

        View createAddressButton = createdView.findViewById(R.id.create_addresss_action_button);

        createAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected AddressIndexPresenter createPresenter() {
        return new AddressIndexPresenter(this, AddressDaoSingleton.getAddressDao(getActivity()));
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

            int positionInList = AddressDataListSingleton.getAddressDao(getActivity()).indexOf(address);
            int listSize = AddressDataListSingleton.getAddressDao(getActivity()).size();

            if (positionInList + 20 > listSize) {
                mPresenter.loadNextPage();
            }

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