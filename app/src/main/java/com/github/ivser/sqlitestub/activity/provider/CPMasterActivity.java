package com.github.ivser.sqlitestub.activity.provider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.data.ProductGenerator;
import com.github.ivser.sqlitestub.model.sqlite.Product;
import com.github.ivser.sqlitestub.model.sqlite.ProductEntry;
import com.github.ivser.sqlitestub.provider.provider.ProductContentProvider;

public class CPMasterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri lastProductUri;
    private ListView list;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        list = (ListView) findViewById(R.id.data);

        String[] from = new String[] {ProductEntry.COLUMN_NAME_TITLE};
        int[] to = new int[] {R.id.title};
        getSupportLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_item, null, from, to, 0);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(CPMasterActivity.this, CPDetailActivity.class);
                Uri todoUri = Uri.parse(ProductContentProvider.CONTENT_URI + "/" + id);
                i.putExtra(ProductContentProvider.CONTENT_ITEM_TYPE, todoUri);
                startActivity(i);
            }
        });
    }

    public void onAddClick(View view) {
        Product product = ProductGenerator.getInstance().next();
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME_TITLE, product.title);
        values.put(ProductEntry.COLUMN_NAME_DESCRIPTION, product.description);
        lastProductUri = getContentResolver().insert(ProductContentProvider.CONTENT_URI, values);
    }

    public void onRemoveLastClick(View view) {
        if (lastProductUri == null) {
            return;
        }
        getContentResolver().delete(lastProductUri, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { ProductEntry._ID, ProductEntry.COLUMN_NAME_TITLE};
        CursorLoader cursorLoader = new CursorLoader(this,
                ProductContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

