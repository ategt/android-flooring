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
    public void showLoading(Integer id) {}

    @Override
    protected int layout() {
        return R.layout.search_input_form;
    }

    @Override
    protected void setUi(View v) {
    }

    public void addListenerOnSpinner() {
        Spinner spinner = (Spinner) getCreatedView().findViewById(R.id.search_option_spinner);
        spinner.setOnItemSelectedListener(new CustomItemSelectedListener());
    }

    public void addListenerOnButton() {
        final Spinner spinner = (Spinner) getCreatedView().findViewById(R.id.search_option_spinner);

        Button button = (Button) getCreatedView().findViewById(R.id.search_submit_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddressSearchByOptionEnum[] addressSearchByOptionEnumArray =
                        new AddressSearchByOptionEnum[]{AddressSearchByOptionEnum.ALL,
                                AddressSearchByOptionEnum.NAME,
                                AddressSearchByOptionEnum.COMPANY,
                                AddressSearchByOptionEnum.NAME_OR_COMPANY,
                                AddressSearchByOptionEnum.STREET,
                                AddressSearchByOptionEnum.CITY,
                                AddressSearchByOptionEnum.STATE,
                                AddressSearchByOptionEnum.ZIP
                        };

                int position = spinner.getSelectedItemPosition();

                EditText editText = (EditText) getView().findViewById(R.id.search_query_input_editText);

                String query = editText.getText().toString();

                mPresenter.launchSearch(query, addressSearchByOptionEnumArray[position]);

                Toast.makeText(getActivity(), "OnClickListener: " +
                        "\nSpinner: " + String.valueOf(spinner.getSelectedItem()) +
                        "\nValue  : " + addressSearchByOptionEnumArray[position].toString(),
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

    @Override
    public void launchSearch(AddressSearchRequest addressSearchRequest) {
        Intent intent = NavUtils.getParentActivityIntent(getActivity());

        intent.putExtra(AddressIndexFragment.EXTRA_ADDRESS_SEARCH_OBJECT, addressSearchRequest);

        NavUtils.navigateUpTo(getActivity(), intent);
    }
}
