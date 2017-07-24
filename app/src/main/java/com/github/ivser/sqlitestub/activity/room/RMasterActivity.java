package com.github.ivser.sqlitestub.activity.room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.data.RoomProductGenerator;
import com.github.ivser.sqlitestub.model.sqlite.ProductEntry;
import com.github.ivser.sqlitestub.db.room.RoomDb;
import com.github.ivser.sqlitestub.model.room.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 24.07.2017.
 */

public class RMasterActivity extends Activity {

    private RoomDb database;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<Product> products;
    private List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        list = (ListView) findViewById(R.id.data);
        database = RoomDb.getDatabase(getApplicationContext());

        readData();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.title, titles);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(RMasterActivity.this, RDetailActivity.class);
                i.putExtra(ProductEntry._ID, products.get(position).id);
                startActivity(i);
            }
        });
    }

    private void readData() {
        products = database.productModel().getAllProducts();
        titles.clear();
        for (Product product : products) {
            titles.add(product.title);
        }
    }

    @Override
    protected void onDestroy() {
        RoomDb.destroyInstance();
        super.onDestroy();
    }

    public void onAddClick(View view) {
        database.productModel().addProduct(RoomProductGenerator.getInstance().next());
        readData();
        adapter.notifyDataSetChanged();
    }

    public void onRemoveLastClick(View view) {
        database.productModel().removeroduct(products.get(products.size()-1));
        readData();
        adapter.notifyDataSetChanged();
    }

}
