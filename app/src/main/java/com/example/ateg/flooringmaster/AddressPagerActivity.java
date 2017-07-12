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
    private List<Address> addresses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addresses = new ArrayList<>();

        viewPager = new ViewPager(this);
        setContentView(viewPager);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Address address = addresses.get(position);
                return AddressFragment.newInstance(address.getId());
            }

            @Override
            public int getCount() {
                return addresses.size();
            }
        });

        Address address = (Address) getIntent().getSerializableExtra(AddressFragment.EXTRA_ADDRESS);

        viewPager.setCurrentItem(addresses.indexOf(address));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Address address1 = addresses.get(position);
                if (address1.getFullName() != null) {
                    setTitle(address1.getFullName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
