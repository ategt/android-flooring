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

    private ValidationException validationException;
    private Context context;
    private Dialog mErrorDialog;

    public ErrorDialog(Context context, ValidationException validationException){
        this.validationException = validationException;
        this.context = context;

        init();
    }

    private void init(){
        List<ValidationError> validationErrors = validationException.getValidationErrorContainer().getErrors();

        String errorMessage = generateMessage(validationErrors);

        mErrorDialog = new Dialog(context, R.style.ValidationErrorDialog);
        mErrorDialog.setContentView(R.layout.validation_errors_dialog);
        mErrorDialog.setTitle(R.string.validation_errors);

        TextView errorText = (TextView) mErrorDialog.findViewById(R.id.validation_dialog_textView);
        errorText.setText(errorMessage);

        Button button = (Button) mErrorDialog.findViewById(R.id.validation_error_dialog_accept_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorDialog.cancel();
            }
        });
    }

    @NonNull
    private String generateMessage(List<ValidationError> validationErrors) {
        String errorMessage = "";

        for (ValidationError validationError : validationErrors) {
            errorMessage += validationError.getFieldName().toUpperCase() + " : "
                    + validationError.getMessage()
                    + System.lineSeparator();
        }
        return errorMessage;
    }
}
