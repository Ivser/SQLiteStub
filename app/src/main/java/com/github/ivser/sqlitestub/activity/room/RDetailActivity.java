package com.github.ivser.sqlitestub.activity.room;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.model.sqlite.ProductEntry;
import com.github.ivser.sqlitestub.db.room.RoomDb;
import com.github.ivser.sqlitestub.model.room.Product;

import java.util.List;

/**
 * DDetailActivity
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class RDetailActivity extends Activity {

    private long id;
    private EditText titleText;
    private EditText descriptionText;
    private RoomDb database;
    private Product currentProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleText = (EditText)findViewById(R.id.title);
        descriptionText = (EditText)findViewById(R.id.description);

        database = RoomDb.getDatabase(getApplicationContext());

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
        List<Product> products = database.productModel().getProduct(id);
        if (products.size() > 0) {
            currentProduct = products.get(0);
            titleText.setText(currentProduct.title);
            descriptionText.setText(currentProduct.description);
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

        currentProduct.title = title;
        currentProduct.description = description;

        database.productModel().updateProduct(currentProduct);
    }


}
