package com.example.niara.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.niara.Dao.CustomerInfoDao;
import com.example.niara.Model.Address;

import org.jetbrains.annotations.NotNull;

@Database(entities = {Address.class}, version = 1)
public abstract class CustomerInfoDatabase extends RoomDatabase {
    private static final String DATABASE_NAME="CustomerInfoDatabase";

    public abstract CustomerInfoDao customerInfoDao();

    private static CustomerInfoDatabase INSTANCE;

    public static CustomerInfoDatabase getInstance(Context context){
        if (INSTANCE==null){
            synchronized (CustomerInfoDatabase.class){
                if (INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context,CustomerInfoDatabase.class,DATABASE_NAME)
                            .addCallback(callback).build();
                }
            }
        }
        return INSTANCE;
    }

    static Callback callback=new Callback() {
        @Override
        public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(INSTANCE);
        }
    };
    static class  PopulateAsyncTask extends AsyncTask<Void,Void,Void>{
        private CustomerInfoDao customerInfoDao;

        PopulateAsyncTask(CustomerInfoDatabase customerInfoDatabase){
            customerInfoDao=customerInfoDatabase.customerInfoDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            customerInfoDao.deleteAll();
            return null;
        }
    }

}
