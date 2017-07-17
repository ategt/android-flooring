package com.example.ateg.flooringmaster;

/**
 * Abstract presenter that provides the view to the specific presenters.
 */
public class ListBasePresenter<T> {

    private T mViewInstance;

    public ListBasePresenter(T viewInstance) {
        this.mViewInstance = viewInstance;
    }

    protected T getView() {
        return mViewInstance;
    }

    public void detachView() {
        mViewInstance = null;
    }
}