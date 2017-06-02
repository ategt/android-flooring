package com.example.ateg.flooringmaster;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 12/23/2016.
 */

public class AddressListActivity extends Activity {

    private static final String TAG = "AddressListActivity";
    private List<Address> addresses = new ArrayList<Address>();
    private final AddressDao addressDao = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set
        //setHasOptionsMenu(true);
        //getActivity().
                setTitle(R.string.address_list);
        //crimes = CrimeLab.get(getActivity()).getCrimes();
        //addresses = this.addressDao = new AddressDaoRemoteImpl(this, new HttpUtilities(this));

//        CrimeAdapter adapter = new CrimeAdapter(crimes);
//
//        setListAdapter(adapter);


        //setRetainInstance(true);
        new FetchAddressesTask().execute(0);

//        thumbnailThread = new ThumbnailDownloader<>(new Handler());
//        thumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
//            @Override
//            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
//                if (isVisible()){
//                    imageView.setImageBitmap(thumbnail);
//                }
//            }
//        });
//
//        thumbnailThread.start();
//        thumbnailThread.getLooper();
        Log.i(TAG, "Background thread started.");
    }

    private class FetchAddressesTask extends AsyncTask<Integer, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(Integer... params) {

            return addressDao.list();
            //FlickrFetchr flickrFetchr = new ();

//            for (Integer parameter : params) {
//                if (parameter != null) {
//                    flickrFetchr.setPageNumber(parameter);
//                    break;
//                }
//            }
//
//            return flickrFetchr.fetchItems();
        }

        @Override
        protected void onPostExecute(List<Address> _addressItems) {
            addresses.addAll(_addressItems);
            setupAdapter();
        }
    }

    protected void setupAdapter() {
        //if (getActivity() == null || gridView == null) return;

        if (addresses != null) {
            list.setAdapter(new GalleryItemAdapter(galleryItems));
            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                        loadAnotherPage();
                    }
                }
            });
        } else {
            gridView.setAdapter(null);
        }
    }


    private class AddressItemAdapter extends ArrayAdapter<Address> {
        public AddressItemAdapter(Context context, int resource, List<Address> objects) {
            super(context, resource, objects);
        }
//        public AddressItemAdapter(List<Address> addressItems) {
//            //super(this, 0, addressItems);
//            //super();
//        }

        //@NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_item_address, parent, false);

            ListView listView = (ListView) convertView.findViewById(android.R.id.list);
            imageView.setImageResource(R.drawable.keep_and_moon);

            GalleryItem galleryItem = getItem(position);
            thumbnailThread.queueThumbnail(imageView, galleryItem.getUrl());

            return convertView;
        }
    }

    //onCreateV
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // Use the floating context menu on froyo and gingerbread devices
            registerForContextMenu(listView);
        } else {
            // Use contextual action bar on honeycomb and higher
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.crime_list_item_context, menu);

                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter crimeAdapter = (CrimeAdapter) getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(getActivity());
                            for (int i = crimeAdapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    crimeLab.delete(crimeAdapter.getItem(i));
                                }
                            }
                            mode.finish();
                            crimeAdapter.notifyDataSetChanged();
                            return true;

                        default:
                            return false;
                    }

                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        View emptyView = (View) view.findViewById(R.id.empty_crime_list);

        listView.setEmptyView(emptyView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);

        // Start Crime Pager Activity
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);

        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(List<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if a view was not input inflate one.
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_address, null);
            }

            // Configure the view for the crime.
            Crime crime = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(crime.getTitle());
            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(DateFormat.getDateFormat(getActivity()).format(crime.getDate()));
            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(crime.isSolved());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_crime:
                createCrime();
                return true;
            case R.id.menu_item_show_subtitles:

                Activity activity = getActivity();
                ActionBar actionBar = activity.getActionBar();

                if (actionBar != null) {

                    if (actionBar.getSubtitle() == null) {
                        actionBar.setSubtitle(R.string.subtitle);
                        menuItem.setTitle(R.string.hide_subtitle);
                    } else {
                        actionBar.setSubtitle(null);
                        menuItem.setTitle(R.string.show_subtitle);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    private void createCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).create(crime);
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = adapterContextMenuInfo.position;
        CrimeAdapter crimeAdapter = (CrimeAdapter) getListAdapter();
        Crime crime = crimeAdapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).delete(crime);
                crimeAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onContextItemSelected(item);
    }
}