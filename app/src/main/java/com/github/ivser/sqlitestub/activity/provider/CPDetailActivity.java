package com.github.ivser.sqlitestub.activity.provider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.model.sqlite.ProductEntry;
import com.github.ivser.sqlitestub.provider.provider.ProductContentProvider;

/**
 * CPDetailActivity
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class CPDetailActivity extends Activity {

    private Uri uri;
    private EditText titleText;
    private EditText descriptionText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleText = (EditText)findViewById(R.id.title);
        descriptionText = (EditText)findViewById(R.id.description);

        if (savedInstanceState != null) {
            uri = savedInstanceState.getParcelable(ProductContentProvider.CONTENT_ITEM_TYPE);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getParcelable(ProductContentProvider.CONTENT_ITEM_TYPE);
        }

        if (uri != null) {
            readData(uri);
        }
    }

    private void readData(Uri uri) {
        String[] projection = {ProductEntry.COLUMN_NAME_TITLE, ProductEntry.COLUMN_NAME_DESCRIPTION};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            titleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_TITLE)));
            descriptionText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_DESCRIPTION)));

            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveProduct();
        outState.putParcelable(ProductContentProvider.CONTENT_ITEM_TYPE, uri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveProduct();
    }

    private void saveProduct() {
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();

        if (description.length() == 0 && title.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME_TITLE, title);
        values.put(ProductEntry.COLUMN_NAME_DESCRIPTION, description);

        if (uri == null) {
            getContentResolver().update(uri, values, null, null);
        }
    }

}
