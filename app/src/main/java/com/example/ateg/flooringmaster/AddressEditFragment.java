package com.example.ateg.flooringmaster;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ateg.flooringmaster.errors.ValidationError;
import com.example.ateg.flooringmaster.errors.ValidationErrorContainer;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ATeg on 7/21/2017.
 */

public class AddressEditFragment extends BaseFragment<AddressEditPresenter> implements AddressEditView {
    private static final String TAG = "Address Edit Fragment";
    public static final String EXTRA_ADDRESS_ID_TO_SHOW = "com.example.ateg.flooringmaster.AddressEditFragment.EXTRA_ADDRESS_ID_TO_SHOW";

    private Integer id;
    private ProgressDialog mSubmittingDialog;
    private Dialog mErrorDialog;

    public void setAddressId(Integer id) {
        this.id = id;
    }

    @Override
    public void showError(Throwable ex) {
        if (mSubmittingDialog != null)
            mSubmittingDialog.dismiss();
        Toast.makeText(getActivity(), "An Error Occurred.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error ocurred.", ex);
    }

    @Override
    public void showLoading(Integer id) {

    }

    @Override
    public void displayAddress(Address address) {
        View view = getCreatedView();

        TextView idTextView = (TextView) view.findViewById(R.id.address_edit_id_textView);
        EditText firstNameEditText = (EditText) view.findViewById(R.id.address_edit_firstName_editTextView);
        EditText lastNameEditText = (EditText) view.findViewById(R.id.address_edit_lastName_editTextView);
        EditText companyEditText = (EditText) view.findViewById(R.id.address_edit_company_editTextView);
        EditText streetNumberEditText = (EditText) view.findViewById(R.id.address_edit_streetNumber_editTextView);
        EditText streetNameEditText = (EditText) view.findViewById(R.id.address_edit_streetName_editTextView);
        EditText cityEditText = (EditText) view.findViewById(R.id.address_edit_city_editTextView);
        EditText stateEditText = (EditText) view.findViewById(R.id.address_edit_state_editTextView);
        EditText zipEditText = (EditText) view.findViewById(R.id.address_edit_zip_editTextView);

        idTextView.setText(address.getId() == null ? getString(R.string.address_id_null) :
                getString(R.string.address_id_prefix) + Integer.toString(address.getId()));

        firstNameEditText.setText(address.getFirstName());
        lastNameEditText.setText(address.getLastName());
        companyEditText.setText(address.getCompany());
        streetNumberEditText.setText(address.getStreetNumber());
        streetNameEditText.setText(address.getStreetName());
        cityEditText.setText(address.getCity());
        stateEditText.setText(address.getState());
        zipEditText.setText(address.getZip());

        View loadingPanel = view.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    public void showSubmitting() {
        mSubmittingDialog = ProgressDialog.show(getActivity(),
                getString(R.string.submit_progress_title),
                getString(R.string.submit_progress_body),
                true);
    }

    @Override
    public void launchShowAddress(Integer addressId) {
        getActivity().finish();

        Intent intent = new Intent(getActivity(), AddressShowActivity.class);
        intent.putExtra(AddressShowFragment.ADDRESS_ID_TO_SHOW, addressId);
        startActivity(intent);
    }

    @Override
    public void displayErrors(ValidationErrorContainer validationErrorContainer) {
        List<ValidationError> validationErrors = validationErrorContainer.getErrors();

        String errorMessage = "";

        for (ValidationError validationError : validationErrors) {
            errorMessage += validationError.getFieldName().toUpperCase() + " : "
                    + validationError.getMessage()
                    + System.lineSeparator();
        }

        mErrorDialog = new Dialog(getActivity());
        mErrorDialog.setContentView(R.layout.validation_errors_dialog);
        mErrorDialog.setTitle(R.string.validation_errors);

        TextView errorText = (TextView) mErrorDialog.findViewById(R.id.validation_dialog_textView);

        Button button = (Button) mErrorDialog.findViewById(R.id.validation_error_dialog_accept_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorDialog.cancel();
            }
        });

    }

    @Override
    protected int layout() {
        return R.layout.fragment_edit_address;
    }

    @Override
    protected void setUi(View v) {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void populate() {

        Intent intent = getActivity().getIntent();

        if (id != null) {
            mPresenter.loadAddress(id);
        } else if (intent.hasExtra(EXTRA_ADDRESS_ID_TO_SHOW)) {
            int id = intent.getIntExtra(EXTRA_ADDRESS_ID_TO_SHOW, 0);
            mPresenter.loadAddress(id);
        }
    }

    @Override
    protected void setListeners() {
        View view = getCreatedView();

        View submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.submitAddress(buildAddressFromForm());
            }
        });
    }

    @Override
    protected AddressEditPresenter createPresenter() {
        return new AddressEditPresenter(this);
    }

    private Address buildAddressFromForm() {
        View view = getView();

        TextView idTextView = (TextView) view.findViewById(R.id.address_edit_id_textView);
        String idString = idTextView.getText().toString();

        TextView firstNameTextView = (TextView) view.findViewById(R.id.address_edit_firstName_editTextView);
        String firstName = firstNameTextView.getText().toString();

        TextView lastNameTextView = (TextView) view.findViewById(R.id.address_edit_lastName_editTextView);
        String lastName = lastNameTextView.getText().toString();

        TextView companyTextView = (TextView) view.findViewById(R.id.address_edit_company_editTextView);
        String company = companyTextView.getText().toString();

        TextView streetNumberTextView = (TextView) view.findViewById(R.id.address_edit_streetNumber_editTextView);
        String streetNumber = streetNumberTextView.getText().toString();

        TextView streetNameTextView = (TextView) view.findViewById(R.id.address_edit_streetName_editTextView);
        String streetName = streetNameTextView.getText().toString();

        TextView cityTextView = (TextView) view.findViewById(R.id.address_edit_city_editTextView);
        String city = cityTextView.getText().toString();

        TextView stateTextView = (TextView) view.findViewById(R.id.address_edit_state_editTextView);
        String state = stateTextView.getText().toString();

        TextView zipTextView = (TextView) view.findViewById(R.id.address_edit_zip_editTextView);
        String zipcode = zipTextView.getText().toString();

        Address address = new Address();

        address.setFirstName(firstName);
        address.setLastName(lastName);
        address.setCompany(company);
        address.setStreetNumber(streetNumber);
        address.setStreetName(streetName);
        address.setCity(city);
        address.setState(state);
        address.setZip(zipcode);

        try {
            idString = idString.replace(getString(R.string.address_id_prefix), "");
            idString = idString.replace(getString(R.string.address_id_label), "");
            idString = idString.trim();

            Integer id = Integer.parseInt(idString);
            address.setId(id);
        } catch (NumberFormatException ex) {

        }

        return address;
    }
}