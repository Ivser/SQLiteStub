package com.github.ivser.sqlitestub.model.sqlite;

import android.provider.BaseColumns;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class ProductEntry implements BaseColumns {
    public static final String TABLE_NAME = "product";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
}