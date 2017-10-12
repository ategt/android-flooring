package com.example.ateg.flooringmaster.errors;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ateg.flooringmaster.R;

import java.util.List;

/**
 * Created by ATeg on 10/11/2017.
 */

public class ErrorDialog {

    public static Dialog BuildErrorDialog(Context context, ValidationException validationException){
        List<ValidationError> validationErrors = validationException.getValidationErrorContainer().getErrors();

        String errorMessage = generateMessage(validationErrors);

        final Dialog errorDialog = new Dialog(context, R.style.ValidationErrorDialog);
        errorDialog.setContentView(R.layout.validation_errors_dialog);
        errorDialog.setTitle(R.string.validation_errors);

        TextView errorText = (TextView) errorDialog.findViewById(R.id.validation_dialog_textView);
        errorText.setText(errorMessage);

        Button button = (Button) errorDialog.findViewById(R.id.validation_error_dialog_accept_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.cancel();
            }
        });

        return errorDialog;
    };

    @NonNull
    private static String generateMessage(List<ValidationError> validationErrors) {
        String errorMessage = "";

        for (ValidationError validationError : validationErrors) {
            errorMessage += validationError.getFieldName().toUpperCase() + " : "
                    + validationError.getMessage()
                    + System.lineSeparator();
        }
        return errorMessage;
    }
}
