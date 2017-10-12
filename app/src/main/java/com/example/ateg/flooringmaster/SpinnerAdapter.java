package com.example.ateg.flooringmaster;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by ATeg on 10/12/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<AddressSearchByOptionEnum> {

    public SpinnerAdapter(@NonNull Context context) {
        super(context, 0, AddressSearchByOptionEnum.values());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) convertView;

        AddressSearchByOptionEnum addressSearchByOptionEnum
                = AddressSearchByOptionEnum.parse(String.valueOf(textView.getText()));

        String content = addressSearchByOptionEnum.ordinal() + " - " + addressSearchByOptionEnum.toString();
        textView.setText(content);

        return textView;
    }
}
