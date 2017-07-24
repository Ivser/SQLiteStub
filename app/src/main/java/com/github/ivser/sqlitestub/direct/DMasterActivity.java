package com.github.ivser.sqlitestub.direct;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.data.ProductGenerator;
import com.github.ivser.sqlitestub.model.Product;
import com.github.ivser.sqlitestub.model.ProductEntry;
import java.util.ArrayList;
import java.util.List;

public class DMasterActivity extends AppCompatActivity {

    private long lastId = 0;
    private ListView list;
    private DataProvider manager;
    private ArrayAdapter<String> adapter;
    private List<Long> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        manager = new DataProvider(this);

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.title, readData());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DMasterActivity.this, DDetailActivity.class);
                i.putExtra(ProductEntry._ID, ids.get(position));
                startActivity(i);
            }
        });
    }

    private List<String> readData() {
        Cursor cursor = manager.readAllProducts();
        ids = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        while(cursor.moveToNext()) {
            Product product = new Product();
            ids.add(cursor.getLong(cursor.getColumnIndexOrThrow(ProductEntry._ID)));
            product.title = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_TITLE));
            product.description = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_DESCRIPTION));
            titles.add(product.title);
        }
        cursor.close();
        return titles;
    }

    @Override
    protected void onDestroy() {
        manager.onDestroy();
        super.onDestroy();
    }

    public void onAddClick(View view) {
        lastId = manager.addProduct(ProductGenerator.getInstance().next());
        readData();
    }

    public void onRemoveLastClick(View view) {
        manager.deleteProduct(lastId);
        readData();
    }
}
