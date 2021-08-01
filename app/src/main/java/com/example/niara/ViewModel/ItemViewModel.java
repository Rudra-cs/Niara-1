package com.example.niara.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.niara.Model.ItemInfo;
import com.example.niara.Repository.ItemInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemInfoRepository itemInfoRepository;
    private LiveData<List<ItemInfo>> getAllItemsInfo;

    public ItemViewModel(@NonNull @NotNull Application application) {
        super(application);

        itemInfoRepository=new ItemInfoRepository(application);
        getAllItemsInfo=itemInfoRepository.getAllItemsinfo();

    }

    public void Insert(ArrayList<ItemInfo> arrayList){
        itemInfoRepository.insert(arrayList);
    }

    public LiveData<List<ItemInfo>> getAllItemInfo(){
        return getAllItemsInfo;
    }
}
