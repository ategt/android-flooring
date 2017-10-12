package com.example.ateg.flooringmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ateg.flooringmaster.errors.ValidationException;

import org.w3c.dom.Text;

/**
 * Created by ATeg on 7/19/2017.
 */

public class AddressCreateFragment extends BaseFragment<AddressCreatePresenter> implements AddressCreateView {

    private final String TAG = "AddressCreateFragment";
    private ProgressDialog mSubmittingDialog;

    @Override
    protected int layout() {
        return R.layout.fragment_edit_address;
    }

    @Override
    protected void setUi(View v) {
        TextView idTextView = (TextView) v.findViewById(R.id.address_edit_id_textView);
        idTextView.setVisibility(View.GONE);

        View loadingPanel = v.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void populate() {

    }

    @Override
    protected void setListeners() {
        Button button = (Button) getCreatedView().findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.createAddress(buildAddressFromForm());
            }
        });
    }

    private Address buildAddressFromForm() {
        View view = getView();

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
    
        return address;
    }

    @Override
    protected AddressCreatePresenter createPresenter() {
        return new AddressCreatePresenter(this);
    }

    @Override
    public void showError(Throwable ex) {
        if (mSubmittingDialog != null)
        mSubmittingDialog.dismiss();
        Log.e(TAG, "Error occured.", ex);
        Toast.makeText(getActivity(), "Error Ocurred!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(Integer id) {

    }

    @Override
    public void addressCreationSuccessful(Address address) {
        if (mSubmittingDialog != null)
            mSubmittingDialog.dismiss();

        Intent intent = new Intent(getActivity(), AddressShowActivity.class);
        intent.putExtra(AddressShowFragment.ADDRESS_ID_TO_SHOW, address.getId());
        intent.putExtra(AddressShowFragment.ADDRESS_TO_SHOW, address);
        startActivity(intent);
    }

    @Override
    public void showSubmitting() {
        mSubmittingDialog = ProgressDialog.show(getActivity(),
                                    getString(R.string.submit_progress_title),
                                    getString(R.string.submit_progress_body),
                                    true);
    }

    @Override
    public void showValidationError(ValidationException ex) {

    }
}
