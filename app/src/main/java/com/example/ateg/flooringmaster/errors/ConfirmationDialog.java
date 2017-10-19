package com.example.ateg.flooringmaster.errors;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ateg.flooringmaster.Address;
import com.example.ateg.flooringmaster.R;

import java.util.List;

/**
 * Created by ATeg on 10/11/2017.
 */

public class ConfirmationDialog {

    public Dialog BuildDialog(Context context, Address address, A) {
        String message = generateMessage(context, address);

        final Dialog confirmDialog = new Dialog(context, R.style.ConfirmDeleteDialog);
        confirmDialog.setContentView(R.layout.confirm_dialog);
        confirmDialog.setTitle(R.string.confirm_title);

        TextView messageText = (TextView) confirmDialog.findViewById(R.id.confirm_dialog_textView);
        messageText.setText(message);

        Button accept = (Button) confirmDialog.findViewById(R.id.confirm_dialog_accept_button);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.cancel();
                mPresenter.delete(address.getId());
            }
        });

        Button cancel = (Button) confirmDialog.findViewById(R.id.confirm_dialog_decline_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.cancel();
                confirm = false;
            }
        });

        return confirmDialog;
    }

    @NonNull
    private static String generateMessage(Context context, Address address) {
        return context.getString(R.string.confirm_delete_dialog,
                address.getFullName(),
                address.getCompany());
    }

    public boolean isConfirm() {
        return confirm;
    }
}
