package com.github.ivser.sqlitestub.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.github.ivser.sqlitestub.model.room.Product;
import com.github.ivser.sqlitestub.model.room.ProductDao;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 24.07.2017.
 */

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    private static final String DB_NAME = "products";
    private static RoomDb instance;

    public abstract ProductDao productModel();

    public static RoomDb getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RoomDb.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
