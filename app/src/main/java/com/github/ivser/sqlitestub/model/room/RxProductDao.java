package com.github.ivser.sqlitestub.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 24.07.2017.
 */
@Dao
public interface RxProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProduct(Product product);

    @Query("select * from product")
    Flowable<List<Product>> getAllProducts();

    @Query("select * from product where id = :id")
    Flowable<Product> getProduct(long id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(Product product);

    @Query("delete from product")
    void removeAllProducts();

    @Delete
    void removeroduct(Product product);
}
