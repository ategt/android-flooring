package com.example.ateg.flooringmaster;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
//import android.support.v7.app.AppCompatActivity;

/**
 * Created by ATeg on 7/10/2017.
 */

public class AddressPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private AddressDao addressDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = new ViewPager(this);
        setContentView(viewPager);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Address address = addressDao.get(position);
                return AddressFragment.newInstance(address.getId());
            }

            @Override
            public int getCount() {
                return addressDao.size();
            }
        });
    }
}
