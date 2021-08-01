package com.example.niara.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.niara.Dao.ItemInfoDao;
import com.example.niara.Model.ItemInfo;

import org.jetbrains.annotations.NotNull;

@Database(entities = {ItemInfo.class}, version = 1)
public abstract class ItemInfoDatabase extends RoomDatabase {

    private static final String DATABASE_NAME="ItemInfoDatabase";

    public abstract ItemInfoDao ItemInfoDao();

    private static ItemInfoDatabase INSTANCE;

    public static ItemInfoDatabase getInstance(Context context){
        if (INSTANCE==null){
            synchronized (ItemInfoDatabase.class){
                if (INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context,ItemInfoDatabase.class,DATABASE_NAME)
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
            new ItemInfoDatabase.PopulateAsyncTask(INSTANCE);
        }
    };
    static class  PopulateAsyncTask extends AsyncTask<Void,Void,Void> {
        private ItemInfoDao itemInfoDao;

        PopulateAsyncTask(ItemInfoDatabase ItemInfoDatabase){
            itemInfoDao=ItemInfoDatabase.ItemInfoDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            itemInfoDao.deleteAll();
            return null;
        }
    }
}
