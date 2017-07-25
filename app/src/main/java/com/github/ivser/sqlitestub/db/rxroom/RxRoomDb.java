package com.github.ivser.sqlitestub.db.rxroom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.github.ivser.sqlitestub.model.room.Product;
import com.github.ivser.sqlitestub.model.room.RxProductDao;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 24.07.2017.
 */

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class RxRoomDb extends RoomDatabase {

    private static final String DB_NAME = "rxroom.products";
    private static RxRoomDb instance;

    public abstract RxProductDao productModel();

    public static RxRoomDb getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RxRoomDb.class, DB_NAME)
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
