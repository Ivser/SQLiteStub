package com.github.ivser.sqlitestub.direct;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.model.Product;
import com.github.ivser.sqlitestub.model.ProductEntry;
import com.github.ivser.sqlitestub.provider.ProductContentProvider;

/**
 * DDetailActivity
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class DDetailActivity extends Activity {

    private long id;
    private EditText titleText;
    private EditText descriptionText;
    private DataProvider manager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleText = (EditText)findViewById(R.id.title);
        descriptionText = (EditText)findViewById(R.id.description);

        manager = new DataProvider(this);

        if (savedInstanceState != null) {
            id = savedInstanceState.getLong(ProductEntry._ID);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong(ProductEntry._ID);
        }

        readData();
    }

    private void readData() {
        Cursor cursor = manager.readProduct(id);
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
        outState.putLong(ProductEntry._ID, id);
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

        manager.updateProduct(values, id);
    }


}
