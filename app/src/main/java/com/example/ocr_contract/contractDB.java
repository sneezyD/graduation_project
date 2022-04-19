package com.example.ocr_contract;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contract.class}, version = 1)
public abstract class contractDB extends RoomDatabase {
    private static contractDB INSTANCE = null;

    public abstract contractDAO contractDAO();


    public static contractDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    contractDB.class, "contract2.db").build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
