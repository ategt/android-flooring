package com.example.ateg.flooringmaster;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by ATeg on 7/11/2017.
 */

public class AddressClient {

    public static final String ADDRESS_SERIALIZABLE_EXTRA = "com.example.ateg.flooringmaster.address";

    private Context context;
    //private static AddressClient addressClient;
    private AddressDao addressDao;
    private String filterString;
    private Integer sizeAtLastCheck;

    public AddressClient(Context context, AddressDao addressDao, String filterString){
        this.context = context;
        this.addressDao = addressDao;
        this.filterString = filterString;
    }

    public void get(Integer id){
        new AddressRestTask().execute(id);
    }

    private class AddressRestTask extends AsyncTask<Integer, Void, Address>{

        @Override
        protected Address doInBackground(Integer... params) {
            Integer id = params[0];
            return addressDao.get(id);
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);

            Intent intent = new Intent(filterString);
            intent.putExtra(ADDRESS_SERIALIZABLE_EXTRA, address);

            // broadcast the completion
            context.sendBroadcast(intent);
        }
    }

    private class AddressSizeTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            return addressDao.size();
        }

        @Override
        protected void onPostExecute(Integer size) {
            sizeAtLastCheck = size;
        }
    }

    public Integer getSize(){
        new AddressSizeTask().execute();

        return sizeAtLastCheck;
    }


//    public static AddressClient get(Context context) {
//        if (addressClient == null) {
//            addressClient = new AddressClient(context.getApplicationContext());
//        }
//        return addressClient;
//    }
}