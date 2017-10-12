package com.example.ateg.flooringmaster;

import com.example.ateg.flooringmaster.errors.ValidationException;

public interface LEView {
    void showError(Throwable ex);
    void showLoading(Integer id);
}