package com.github.ivser.sqlitestub.direct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.ivser.sqlitestub.db.DbHelper;
import com.github.ivser.sqlitestub.model.Product;
import com.github.ivser.sqlitestub.model.ProductEntry;

/**
 * DataProvider
 * <p>
 * Created by SIvanov on 20.07.2017.
 */

public class DataProvider {

    private DbHelper mDbHelper;

    public DataProvider(Context context) {
        mDbHelper = new DbHelper(context);
    }

    public long addProduct(Product product) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME_TITLE, product.title);
        values.put(ProductEntry.COLUMN_NAME_DESCRIPTION, product.description);

        return db.insert(ProductEntry.TABLE_NAME, null, values);
    }

    public Cursor readProduct(long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {ProductEntry._ID, ProductEntry.COLUMN_NAME_TITLE, ProductEntry.COLUMN_NAME_DESCRIPTION};

        String selection = ProductEntry._ID + " = " + id;
        String sortOrder = ProductEntry.COLUMN_NAME_DESCRIPTION + " DESC";

        return db.query(ProductEntry.TABLE_NAME, projection, selection, null, null, null, sortOrder);
    }

    public Cursor readAllProducts() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {ProductEntry._ID, ProductEntry.COLUMN_NAME_TITLE, ProductEntry.COLUMN_NAME_DESCRIPTION};

        String sortOrder = ProductEntry.COLUMN_NAME_DESCRIPTION + " DESC";

        return db.query(ProductEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
    }

    public void deleteProduct(long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = ProductEntry._ID + " = " + id;
        db.delete(ProductEntry.TABLE_NAME, selection, null);
    }

    public int updateProduct(ContentValues values, long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = ProductEntry._ID + " = " + id;

        return db.update(ProductEntry.TABLE_NAME, values, selection, null);
    }


    public void onDestroy() {
        mDbHelper.close();
    }
}
