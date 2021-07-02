package com.example.niara.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.niara.Model.Address;
import com.example.niara.Repository.CustomerInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomerInfoViewModel extends AndroidViewModel {

    private CustomerInfoRepository customerInfoRepository;
    private LiveData<List<Address>> getAllCustomersInfo;

    public CustomerInfoViewModel(@NonNull @NotNull Application application) {
        super(application);

        customerInfoRepository=new CustomerInfoRepository(application);
        getAllCustomersInfo=customerInfoRepository.getAllCustomersinfo();

    }

    public void Insert(ArrayList<Address> arrayList){
        customerInfoRepository.insert(arrayList);
    }

    public LiveData<List<Address>> getAllCustomerInfo(){
        return getAllCustomersInfo;
    }
}
