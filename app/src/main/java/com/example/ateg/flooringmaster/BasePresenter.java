package com.example.ateg.flooringmaster;

/**
 * Abstract presenter that provides the view to the specific presenters.
 */
public class BasePresenter<T> {

    private T mViewInstance;

    public BasePresenter(T viewInstance) {
        this.mViewInstance = viewInstance;
    }

    protected T getView() {
        return mViewInstance;
    }

    public void detachView() {
        mViewInstance = null;
    }

    public void attachView(T viewInstance){
        this.mViewInstance = viewInstance;
    }
}