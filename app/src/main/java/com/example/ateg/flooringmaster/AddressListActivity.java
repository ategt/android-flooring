package com.example.ateg.flooringmaster;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ATeg on 12/23/2016.
 */

public class AddressListActivity extends ListActivity {

    private static final String TAG = "AddressListActivity";
    private AddressDao addressDao;

    @Override
    public void onResume() {
        super.onResume();
        ((AddressAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setTitle(R.string.address_list);

        List<Address> addresses = AddressDaoLocalImpl.getLocalDao().list();

        Uri submittedUri = Uri.parse(getString(R.string.starting_root_url));

        this.addressDao = new AddressDaoRemoteImpl(this, new HttpUtilities(this, submittedUri));

        new FetchAddressesTask().execute(0);

        AddressAdapter addressAdapter = new AddressAdapter(this, 0, addresses);
        setListAdapter(addressAdapter);

        Log.i(TAG, "Background thread started.");
    }

    private class FetchAddressesTask extends AsyncTask<Integer, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(Integer... params) {
            List<Address> addressListResult = addressDao.list();
            return addressListResult;
        }

        @Override
        protected void onPostExecute(List<Address> _addressItems) {
            AddressDaoLocalImpl.getLocalDao(_addressItems);
            ((AddressAdapter) getListAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(name, context, attrs);
        Log.d(TAG, name);

        return view;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AddressAdapter addressAdapter = (AddressAdapter) getListAdapter();
        Address selectedAddress = addressAdapter.getItem(position);

        Intent intent = new Intent(this, AddressPagerActivity.class);
        intent.putExtra(AddressFragment.EXTRA_ADDRESS, selectedAddress);
        intent.putExtra(AddressFragment.EXTRA_ADDRESS_ID, selectedAddress.getId());

        startActivity(intent);
    }

    private class AddressAdapter extends ArrayAdapter<Address> {
        public AddressAdapter(Context context, int resource, List<Address> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if a view was not input inflate one.
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.list_item_address, null);
            }

            // Configure the view for the crime.
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