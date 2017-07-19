package com.example.ateg.flooringmaster;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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

        addressDao = AddressDaoSingleton.getAddressDao(this);

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Address address = AddressDataListSingleton.getOrNull(position);

                Integer addressId = null;
                if (address != null)
                    addressId = address.getId();

                return AddressSupportFragment.newInstance(addressId);
            }

            @Override
            public int getCount() {
                return addressDao.size();
            }
        });

        Address address = (Address) getIntent().getSerializableExtra(AddressSupportFragment.EXTRA_ADDRESS);

        int itemPosition = AddressDataListSingleton.getAddressDao(getApplicationContext()).indexOf(address);
        viewPager.setCurrentItem(itemPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Address address1 = AddressDataListSingleton.getOrNull(position);

                if (address1 != null)
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