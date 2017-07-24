package com.github.ivser.sqlitestub.provider.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.github.ivser.sqlitestub.db.sqlite.DbHelper;
import com.github.ivser.sqlitestub.model.sqlite.ProductEntry;

import java.util.Arrays;
import java.util.HashSet;

/**
 * sqliteContentProviderTest
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class ProductContentProvider extends ContentProvider {

    private DbHelper mDbHelper;

    // used for the UriMacher
    private static final int PRODUCTS = 10;
    private static final int PRODUCT_ID = 20;

    private static final String AUTHORITY = "com.github.ivser.sqlitestub";

    private static final String BASE_PATH = "products";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/products";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/product";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, PRODUCTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns(projection);

        queryBuilder.setTables(ProductEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PRODUCTS:
                break;
            case PRODUCT_ID:
                queryBuilder.appendWhere(ProductEntry._ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case PRODUCTS:
                id = db.insert(ProductEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case PRODUCTS:
                rowsDeleted = db.delete(ProductEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case PRODUCT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(
                            ProductEntry.TABLE_NAME,
                            ProductEntry._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = db.delete(
                            ProductEntry.TABLE_NAME,
                            ProductEntry._ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case PRODUCTS:
                rowsUpdated = db.update(ProductEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PRODUCT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(ProductEntry.TABLE_NAME,
                            values,
                            ProductEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(ProductEntry.TABLE_NAME,
                            values,
                            ProductEntry._ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {ProductEntry._ID,
                ProductEntry.COLUMN_NAME_TITLE,
                ProductEntry.COLUMN_NAME_DESCRIPTION};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}
