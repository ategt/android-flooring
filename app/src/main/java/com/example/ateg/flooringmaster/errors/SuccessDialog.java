package com.example.ateg.flooringmaster.errors;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ateg.flooringmaster.Address;
import com.example.ateg.flooringmaster.R;

/**
 * Created by ATeg on 10/11/2017.
 */

public class SuccessDialog {

    public static Dialog BuildDialog(Context context, Address address){
        String message = generateMessage(context, address);

        final Dialog confirmDialog = new Dialog(context, R.style.ConfirmDeleteDialog);
        confirmDialog.setContentView(R.layout.confirm_dialog);
        confirmDialog.setTitle(R.string.confirm_title);

        TextView messageText = (TextView) confirmDialog.findViewById(R.id.confirm_dialog_textView);
        messageText.setText(message);

        Button button = (Button) confirmDialog.findViewById(R.id.validation_error_dialog_accept_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.cancel();
            }
        });

        return confirmDialog;
    };

    @NonNull
    private static String generateMessage(Context context, Address address) {
        return context.getString(R.string.confirm_delete_dialog,
                address.getFullName(),
                address.getCompany());
    }
}
