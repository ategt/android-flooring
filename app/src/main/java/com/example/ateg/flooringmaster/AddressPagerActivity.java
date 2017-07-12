package com.example.ateg.flooringmaster;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 7/10/2017.
 */

public class AddressPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    //private List<Address> addresses;
    private AddressDao addressDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addressDao = AddressDaoLocalImpl.getLocalDao();

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Address address = addressDao.list().get(position);
                //Address address = addresses.get(position);
                return AddressFragment.newInstance(address.getId());
            }

            @Override
            public int getCount() {
                return addressDao.list().size();
            }
        });

        Address address = (Address) getIntent().getSerializableExtra(AddressFragment.EXTRA_ADDRESS);

        int itemPosition = addressDao.list().indexOf(address);
        viewPager.setCurrentItem(itemPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Address address1 = addressDao.list().get(position);
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
