package com.example.niara.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.niara.Model.ItemInfo;

import java.util.List;

@Dao
public interface ItemInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ItemInfo> iteminfolist);

    @Query("SELECT * FROM iteminfo")
    LiveData<List<ItemInfo>> getAllItemsinfo();

    @Query("DELETE FROM iteminfo")
    void deleteAll();
}
