package com.example.ateg.flooringmaster;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import static com.example.ateg.flooringmaster.R.id.viewPager;

/**
 * Created by ATeg on 7/10/2017.
 */

public class AddressPagerFragment extends BaseFragment<AddressPagerPresenter> implements AddressPagerView {

    //private ViewPager viewPager;
    //private List<Address> addresses;
    //private AddressDao addressDao;

    @Deprecated
    public void NotAThing(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);

        addressDao = AddressDaoSingleton.getAddressDao(this);

        viewPager = new ViewPager(this);
        viewPager.setId(viewPager);
        setContentView(viewPager);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Address address = AddressDataListSingleton.getOrNull(position);

                Integer addressId = null;
                if (address != null)
                    addressId = address.getId();

                return AddressFragment.newInstance(addressId);
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

    @Override
    protected int layout() {
        return R.layout.address_pager_fragment;
    }

    @Override
    protected void setUi(View v) {
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.address_pager_fragment_index_viewPager);

        final AddressDao addressDao = AddressDaoSingleton.getAddressDao(null);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return AddressDataListSingleton.size();
            }

            @Override
            public android.app.Fragment getItem(int position) {
                Address address = AddressDataListSingleton.getOrNull(position);

                Integer addressId = null;
                if (address != null)
                    addressId = address.getId();

                return AddressShowFragment.newInstance(addressId);
            }
        });
    }

    @Override
    protected void init() {
        addressDao = AddressDaoSingleton.getAddressDao(getActivity());

        //viewPager = new ViewPager(this);
        //viewPager.setId(R.id.viewPager);
        //setContentView(viewPager);

        //android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Address address = AddressDataListSingleton.getOrNull(position);

                Integer addressId = null;
                if (address != null)
                    addressId = address.getId();

                return AddressFragment.newInstance(addressId);
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

    @Override
    protected void populate() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected AddressPagerPresenter createPresenter() {
        return null;
    }

    @Override
    public void showError(Throwable ex) {

    }

    @Override
    public void showLoading(Integer id) {

    }

    @Override
    public void displayAddress(Address address) {

    }
}