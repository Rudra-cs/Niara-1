package com.example.niara.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.niara.Dao.CustomerInfoDao;
import com.example.niara.Database.CustomerInfoDatabase;
import com.example.niara.Model.Address;

import java.util.ArrayList;
import java.util.List;

public class CustomerInfoRepository {
    private CustomerInfoDatabase database;
    private LiveData<List<Address>> getAllCustomersinfo;
    public CustomerInfoRepository(Application application){
        database=CustomerInfoDatabase.getInstance(application);
        getAllCustomersinfo=database.customerInfoDao().getAllCustomersinfo();


    }

    public void insert(List<Address> customerslist){
        new InsertAsyncTask(database).execute(customerslist);
    }

    public LiveData<List<Address>> getAllCustomersinfo(){
        return getAllCustomersinfo;
    }

    static class InsertAsyncTask extends AsyncTask<List<Address>,Void,Void>{

        private CustomerInfoDao customerInfoDao;
        InsertAsyncTask(CustomerInfoDatabase customerInfoDatabase){
            customerInfoDao=customerInfoDatabase.customerInfoDao();

        }

        @Override
        protected Void doInBackground(List<Address>... arrayLists) {
            customerInfoDao.insert(arrayLists[0]);
            return null;
        }
    }
}
