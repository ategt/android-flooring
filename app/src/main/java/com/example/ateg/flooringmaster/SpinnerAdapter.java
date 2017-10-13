package com.example.ateg.flooringmaster;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by ATeg on 10/12/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<AddressSearchByOptionEnum> {

    private AddressSearchByOptionEnum[] addressSearchByOptionEnum;
    private Context context;
    private int resource;

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull AddressSearchByOptionEnum[] objects) {
        super(context, resource, objects);
        this.addressSearchByOptionEnum = objects;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) convertView;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            textView = (TextView) inflater.inflate(resource, parent, false);
        }

//        if (addressSearchByOptionEnum != null){
//            AddressSearchByOptionEnum addressSearchByOptionEnum
//                    //= AddressSearchByOptionEnum.parse(String.valueOf(textView.getText()));
//                    = addressSearchByOptionEnum[position];
//        }else {
//            throw new RuntimeException("AddressSearchByOptionEnum is null, it should never be null.");
//        }

        AddressSearchByOptionEnum addressSearchByOptionEnum = AddressSearchByOptionEnum.values()[position];
                    //= addressSearchByOptionEnum == null ? AddressSearchByOptionEnum.ALL : addressSearchByOptionEnum[position];
                //= addressSearchByOptionEnum[position];


        //String content = addressSearchByOptionEnum.ordinal() + " - " + addressSearchByOptionEnum.toString();
        String content = " - " + addressSearchByOptionEnum.toString() + " - ";
        textView.setText(content);

        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) convertView;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            textView = (TextView) inflater.inflate(resource, parent, false);
        }

        AddressSearchByOptionEnum addressSearchByOptionEnum = AddressSearchByOptionEnum.values()[position];

        String content = addressSearchByOptionEnum.ordinal() + " - " + addressSearchByOptionEnum.toString();
        textView.setText(content);

        textView.setPadding(10, 10, 10, 10);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        textView.setHighlightColor(Color.GREEN);
        textView.setBackgroundColor(Color.MAGENTA);
        textView.setTextColor(Color.RED);

        return textView;
    }
}
