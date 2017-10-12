package com.example.ateg.flooringmaster;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract presenter that provides the view to the specific presenters.
 */
public class BasePresenter<T> {

    private List<AsyncTask> networkCalls;

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

    public AsyncTask registerNetworkCall(AsyncTask asyncTask){
        if (networkCalls == null)
            networkCalls = establishNetworkCalls();

        networkCalls.add(asyncTask);
        return asyncTask;
    }

    private List<AsyncTask> establishNetworkCalls() {
        return new ArrayList<>();
    }

    public void cancelNetworkCalls(){
        for (AsyncTask asyncTask : networkCalls){
            asyncTask.cancel(true);
        }
    }
}