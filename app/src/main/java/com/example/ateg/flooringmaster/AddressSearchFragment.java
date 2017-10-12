package com.example.ateg.flooringmaster;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ateg.flooringmaster.errors.ErrorDialog;
import com.example.ateg.flooringmaster.errors.ValidationException;

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
        Spinner spinner = (Spinner) v.findViewById(R.id.search_option_spinner);
        spinner.setAdapter(new ArrayAdapter<AddressSearchByOptionEnum>(getActivity(), 0, AddressSearchByOptionEnum.values()));
    }

    @Override
    protected void init() {

    }

    @Override
    protected void populate() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected AddressSearchPresenter createPresenter() {
        return new AddressSearchPresenter(this);
    }
}
