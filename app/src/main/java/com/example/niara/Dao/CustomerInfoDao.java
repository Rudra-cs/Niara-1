package com.example.niara.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.niara.Model.Address;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CustomerInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Address> customerinfolist);

    @Query("SELECT * FROM customerinfo")
    LiveData<List<Address>> getAllCustomersinfo();

    @Query("DELETE FROM customerinfo")
    void deleteAll();
}
