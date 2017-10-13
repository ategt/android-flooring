package com.example.ateg.flooringmaster;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.ateg.flooringmaster.errors.ErrorDialog;
import com.example.ateg.flooringmaster.errors.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 10/12/2017.
 */

public class AddressSearchFragment extends BaseFragment<AddressSearchPresenter> implements AddressSearchView {
    private static final String TAG = "Address Search Fragment";

    @Override
    public void showError(Throwable ex) {
        Log.e(TAG, "Something is wrong.", ex);

        if (ex.getClass().isInstance(ValidationException.class)) {
            ValidationException validationException = (ValidationException) ex;

            ErrorDialog.BuildErrorDialog(getActivity(), validationException).show();
        } else
            Toast.makeText(getActivity(), "An Error Occurred.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(Integer id) {

    }

    @Override
    protected int layout() {
        return R.layout.search_input_form;
    }

    @Override
    protected void setUi(View v) {
        Spinner spinner = (Spinner) v.findViewById(R.id.search_option_spinner_2);

        //spinner.setAdapter(new SpinnerAdapter(getActivity()));

//        List<String> list = new ArrayList<>();
//
//        for (AddressSearchByOptionEnum addressSearchByOptionEnum : AddressSearchByOptionEnum.values()) {
//            list.add(addressSearchByOptionEnum.toString());
//        }

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.simple_spinner_starter, list);

        SpinnerAdapter dataAdapter = new SpinnerAdapter(getActivity(),
                R.layout.simple_spinner_starter,
                AddressSearchByOptionEnum.values());

        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinner() {
        Spinner spinner = (Spinner) getCreatedView().findViewById(R.id.search_option_spinner);
        spinner.setOnItemSelectedListener(new CustomItemSelectedListener());
    }

    public void addListenerOnButton() {
        final Spinner spinner = (Spinner) getCreatedView().findViewById(R.id.search_option_spinner);
        final Spinner spinner2 = (Spinner) getCreatedView().findViewById(R.id.search_option_spinner_2);

        Button button = (Button) getCreatedView().findViewById(R.id.search_submit_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "OnClickListener: " +
                                "\nSpinner 1: " + String.valueOf(spinner.getSelectedItem()) +
                                "\nSpinner 2: " + String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    protected void populate() {

    }

    @Override
    protected void setListeners() {
        addListenerOnSpinner();
        addListenerOnButton();

        Button button = (Button) getCreatedView().findViewById(R.id.search_return_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected AddressSearchPresenter createPresenter() {
        return new AddressSearchPresenter(this, AddressDaoSingleton.getAddressDao(getActivity()));
    }

    private void buildSearch(String query, AddressSearchByOptionEnum addressSearchByOptionEnum) {
        AddressSearchRequest addressSearchRequest = new AddressSearchRequest(query, addressSearchByOptionEnum);

        addressSearchRequest
    }

    @Override
    public void searchResults(List<Address> addressList) {
        Intent intent = NavUtils.getParentActivityIntent(getActivity());

        intent.

        NavUtils.navigateUpTo(getActivity(), intent);
    }
}
