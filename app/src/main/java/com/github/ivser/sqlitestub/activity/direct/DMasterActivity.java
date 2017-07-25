package com.github.ivser.sqlitestub.activity.direct;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.data.ProductGenerator;
import com.github.ivser.sqlitestub.model.sqlite.Product;
import com.github.ivser.sqlitestub.model.sqlite.ProductEntry;
import com.github.ivser.sqlitestub.provider.direct.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class DMasterActivity extends AppCompatActivity {

    private long lastId = 0;
    private DataProvider manager;
    private ArrayAdapter<String> adapter;
    private List<Long> ids;
    private List<String> titles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ListView list = (ListView) findViewById(R.id.data);

        manager = new DataProvider(this);
        readData();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.title, titles);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(DMasterActivity.this, DDetailActivity.class);
            i.putExtra(ProductEntry._ID, ids.get(position));
            startActivity(i);
        });
    }

    private void readData() {
        Cursor cursor = manager.readAllProducts();
        ids = new ArrayList<>();
        titles.clear();
        while(cursor.moveToNext()) {
            Product product = new Product();
            ids.add(cursor.getLong(cursor.getColumnIndexOrThrow(ProductEntry._ID)));
            product.title = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_TITLE));
            product.description = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_DESCRIPTION));
            titles.add(product.title);
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        manager.onDestroy();
        super.onDestroy();
    }

    public void onAddClick(View view) {
        lastId = manager.addProduct(ProductGenerator.getInstance().next());
        readData();
        adapter.notifyDataSetChanged();
    }

    public void onRemoveLastClick(View view) {
        manager.deleteProduct(lastId);
        readData();
        adapter.notifyDataSetChanged();
    }
}
