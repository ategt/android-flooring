package com.example.ateg.flooringmaster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ateg.flooringmaster.errors.ErrorDialog;
import com.example.ateg.flooringmaster.errors.ValidationException;

import java.util.List;

/**
 * Created by ATeg on 7/17/2017.
 */

public class AddressIndexFragment extends BaseFragment<AddressIndexPresenter> implements AddressIndexView, AddressAppendListener {

    public static final String ADDRESS_ID_TO_SHOW = "com.example.ateg.flooringmaster.ADDRESS_ID_TO_SHOW";
    public static final String EXTRA_ADDRESS_SEARCH_OBJECT = "com.example.ateg.flooringmaster.AddressIndexFragment.EXTRA_ADDRESS_SEARCH_OBJECT";

    private static final String TAG = "AddressListMvpActivity";
    private ProgressDialog mLoadingDialog;

    @Override
    public void onResume() {
        super.onResume();
        //((AddressIndexFragment.AddressAdapter) getListAdapter()).notifyDataSetChanged();
        scrollToId();

        evaluateVisibilityOfResetButton();
    }

    private void scrollToId() {
        Intent intent = getActivity().getIntent();

        if (intent.hasExtra(ADDRESS_ID_TO_SHOW)) {
            Integer id = intent.getIntExtra(ADDRESS_ID_TO_SHOW, 0);
            ListView listView = (ListView) getCreatedView().findViewById(R.id.address_index_listView);
            ListAdapter listAdapter = listView.getAdapter();

            if (listAdapter != null) {
                mPresenter.scrollToId(id);
            }
        }
    }

    @Override
    public void showError(Throwable ex) {
        Log.e(TAG, "Something is wrong.", ex);

        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();

        if (ex.getClass().isInstance(ValidationException.class)) {
            ErrorDialog.BuildErrorDialog(getActivity(), (ValidationException) ex).show();
        } else
            Toast.makeText(getActivity(), "An Error Occurred.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(Integer id) {
        mLoadingDialog = ProgressDialog.show(getActivity(),
                getString(R.string.submit_progress_title),
                getString(R.string.submit_progress_body),
                true);
    }

    @Override
    public void appendAddresses(List<Address> addressList) {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();

        ListView listView = (ListView) getCreatedView().findViewById(R.id.address_index_listView);

        ListAdapter listAdapter = listView.getAdapter();
        AddressAdapter addressAdapter = (AddressAdapter) listAdapter;

        if (listAdapter == null) {
        } else {
            AddressDataListSingleton.appendAll(addressList);
            addressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void scrollToAddress(Address address) {
        int position = AddressDataListSingleton.indexOf(address);
        if (position > 0) {
            ListView listView = (ListView) getCreatedView().findViewById(R.id.address_index_listView);
            listView.setSelection(position);
            listView.smoothScrollToPosition(position);

            Intent intent = getActivity().getIntent();

            if (intent.hasExtra(ADDRESS_ID_TO_SHOW)) {
                intent.removeExtra(ADDRESS_ID_TO_SHOW);
            }
        } else {
            mPresenter.loadNextPage();
            mPresenter.addAddressesAppenededListener(this);
            //mPresenter.scrollToId(address.getId());
        }
    }

    @Override
    public void resetList() {
        ListView listView = (ListView) getCreatedView().findViewById(R.id.address_index_listView);
        ListAdapter listAdapter = listView.getAdapter();

        AddressAdapter addressAdapter = (AddressAdapter) listAdapter;

        if (addressAdapter != null)
            addressAdapter.notifyDataSetChanged();

        evaluateVisibilityOfResetButton();
        populate();
    }

    @Override
    protected int layout() {
        return R.layout.list_addresses;
    }

    @Override
    protected void setUi(View v) {
        ListView listView = (ListView) v.findViewById(R.id.address_index_listView);
        listView.setEmptyView(v.findViewById(R.id.address_list_empty));

        //evaluateVisibilityOfResetButton(v);
    }

    private void evaluateVisibilityOfResetButton() {
        evaluateVisibilityOfResetButton(getCreatedView());
    }

    private void evaluateVisibilityOfResetButton(View v) {
        FloatingActionButton resetButton = (FloatingActionButton) v.findViewById(R.id.addresss_index_clear_action_button);

        AddressSearchRequest addressSearchRequest = AddressSearchRequestSingleton.getAddressSearchRequest();

        resetButton.setVisibility(addressSearchRequest == null ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void init() {

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(EXTRA_ADDRESS_SEARCH_OBJECT)) {
            mPresenter.setAddressSearchRequest(
                    (AddressSearchRequest) intent.getSerializableExtra(EXTRA_ADDRESS_SEARCH_OBJECT));
        }

        getActivity().setTitle(R.string.address_list);

        AddressIndexFragment.AddressAdapter addressAdapter
                = new AddressIndexFragment.AddressAdapter(
                                                getActivity(),
                                                0,
                                                AddressDataListSingleton.getDataList(getActivity())
                                            );

        ListView listView = (ListView) getCreatedView().findViewById(R.id.address_index_listView);
        listView.setAdapter(addressAdapter);
    }

    @Override
    protected void populate() {
        //mPresenter.loadAddresses(AddressDataListSingleton.getResultProperties());
        //mPresenter.loadAddresses(new AddressResultSegment(AddressSortByEnum.SORT_BY_ID, 0 , 100));
        //mPresenter.loadAddresses(new ResultProperties(AddressSortByEnum.SORT_BY_ID, 0, 100));
        mPresenter.initialAddressLoad();
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

                Intent intent = new Intent(getActivity(), AddressPagerActivity.class);
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

        View searchAddressButton = createdView.findViewById(R.id.addresss_index_search_action_button);
        searchAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressSearchActivity.class);
                startActivity(intent);
            }
        });

        View clearSearchButton = createdView.findViewById(R.id.addresss_index_clear_action_button);
        clearSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clearSearch();
            }
        });
    }

    @Override
    protected AddressIndexPresenter createPresenter() {
        return new AddressIndexPresenter(this, AddressDaoSingleton.getAddressDao(getActivity()));
    }

    @Override
    public void onAddressesAppended() {
        scrollToId();
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

            Log.d(TAG, "Position: " + position);

            Address address = getItem(position);

            int positionInList = AddressDataListSingleton.indexOf(address);
            int listSize = AddressDataListSingleton.size();

            mPresenter.considerLoadingNextPage(positionInList, listSize);

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