package com.NiaraFoodJoint.niara.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.NiaraFoodJoint.niara.Dao.ItemInfoDao;
import com.NiaraFoodJoint.niara.Database.ItemInfoDatabase;
import com.NiaraFoodJoint.niara.Model.ItemInfo;

import java.util.List;

public class ItemInfoRepository {
    private ItemInfoDatabase database;
    private LiveData<List<ItemInfo>> getAllItemsinfo;
    public ItemInfoRepository(Application application){
        database=ItemInfoDatabase.getInstance(application);
        getAllItemsinfo=database.ItemInfoDao().getAllItemsinfo();


    }

    public void insert(List<ItemInfo> itemlist){
        new InsertAsyncTask(database).execute(itemlist);
    }

    public LiveData<List<ItemInfo>> getAllItemsinfo(){
        return getAllItemsinfo;
    }

    static class InsertAsyncTask extends AsyncTask<List<ItemInfo>,Void,Void> {

        private ItemInfoDao itemInfoDao;
        InsertAsyncTask(ItemInfoDatabase itemInfoDatabase){
            itemInfoDao=itemInfoDatabase.ItemInfoDao();

        }

        @Override
        protected Void doInBackground(List<ItemInfo>... arrayLists) {
            itemInfoDao.insert(arrayLists[0]);
            return null;
        }
    }
}
