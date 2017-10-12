package com.example.ateg.flooringmaster;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by ATeg on 10/12/2017.
 */

class CustomItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(), "OnClickListener: " +
                        "\nSpinner 1: " + String.valueOf(parent.getSelectedItem()) +
                        "\nSpinner ID: " + String.valueOf(parent.getSelectedItemId()) +
                        "\nSpinner Pos: " + parent.getSelectedItemPosition(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
