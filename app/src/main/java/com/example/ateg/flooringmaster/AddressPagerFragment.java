package com.example.ateg.flooringmaster;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import static com.example.ateg.flooringmaster.R.id.viewPager;

/**
 * Created by ATeg on 7/10/2017.
 */

public class AddressPagerFragment extends BaseFragment<AddressPagerPresenter> implements AddressPagerView {

    public static final String PAGER_POSITION_TO_SAVE = "com.example.ateg.flooringmaster.AddressPagerFragment.PAGER_POSITION";

    private static final String TAG = "AddressPagerFragment";

    private ViewPager viewPager;

    @Override
    protected int layout() {
        return R.layout.address_pager_fragment;
    }

    @Override
    protected void setUi(View v) {
        viewPager = (ViewPager) v.findViewById(R.id.address_pager_fragment_index_viewPager);

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

                AddressShowFragment addressShowFragment = new AddressShowFragment();
                addressShowFragment.setAddressIdToShow(addressId);
                return addressShowFragment;
            }
        });

        Intent intent = getActivity().getIntent();

        Address targetAddress = null;
        if (intent.hasExtra(AddressShowFragment.ADDRESS_TO_SHOW)) {
            Serializable serializabled = intent.getSerializableExtra(AddressShowFragment.ADDRESS_TO_SHOW);
            targetAddress = (Address) serializabled;
        } else if (intent.hasExtra(AddressShowFragment.ADDRESS_ID_TO_SHOW)) {
            int id = intent.getIntExtra(AddressShowFragment.ADDRESS_ID_TO_SHOW, 0);
            AddressClient addressClient = AddressDaoSingleton.getAddressDao(getActivity());
            targetAddress = addressClient.get(id);
        }

        if (targetAddress != null) {
            int itemPosition = AddressDataListSingleton.indexOf(targetAddress);
            viewPager.setCurrentItem(itemPosition);
        }
    }

    @Override
    protected void init() {
    }

    @Override
    protected void populate() {

    }

    @Override
    protected void setListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Address address1 = AddressDataListSingleton.getOrNull(position);

                if (address1 != null)
                    if (address1.getFullName() != null) {
                        getActivity().setTitle(address1.getFullName());
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected AddressPagerPresenter createPresenter() {
        return new AddressPagerPresenter(this);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGER_POSITION_TO_SAVE, viewPager.getCurrentItem());

        Intent intent = getActivity().getIntent();

        Address address = AddressDataListSingleton.getOrNull(viewPager.getCurrentItem());

        intent.putExtra(AddressShowFragment.ADDRESS_ID_TO_SHOW, address.getId());
        intent.putExtra(AddressShowFragment.ADDRESS_TO_SHOW, address);
    }
}