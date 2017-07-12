package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 7/10/2017.
 */

public class AddressPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private AddressDao addressDao;
    //private LruCache<Integer, Address> addressLruCache;
    private AddressClient addressClient;
    private List<Address> addresses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addresses = new ArrayList<>();

        viewPager = new ViewPager(this);
        setContentView(viewPager);

        String baseUriString = getString(R.string.starting_root_url);
        Uri baseUri = Uri.parse(baseUriString);

        addressClient = new AddressClient(this,
                new AddressDaoRemoteImpl(this, new HttpUtilities(this, baseUri)),
                "");

        //addressLruCache = new LruCache<>(20);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                //Address address = addressDao.get(position);
                //addressLruCache.get())
                Address address = addresses.get(position);

                return AddressFragment.newInstance(address.getId());
            }

            @Override
            public int getCount() {
                //return addressLruCache.size();
                //return addressDao.size();
                //return addressClient.getSize();
                return addresses.size();
            }
        });
    }
}
