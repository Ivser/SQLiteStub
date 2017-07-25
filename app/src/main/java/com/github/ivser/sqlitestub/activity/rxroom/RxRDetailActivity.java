package com.github.ivser.sqlitestub.activity.rxroom;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.db.rxroom.RxRoomDb;
import com.github.ivser.sqlitestub.model.room.Product;

/**
 * DDetailActivity
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class RxRDetailActivity extends Activity {

    private static final String TAG = RxRDetailActivity.class.getName();

    private long id;
    private EditText titleText;
    private EditText descriptionText;
    private RxRoomDb database;
    private Product currentProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleText = (EditText)findViewById(R.id.title);
        descriptionText = (EditText)findViewById(R.id.description);

        database = RxRoomDb.getDatabase(getApplicationContext());

        if (savedInstanceState != null) {
            id = savedInstanceState.getLong("ID");
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("ID");
        }

        readData();
    }

    private void readData() {
        database.productModel().getProduct(id).subscribe(product -> {
            currentProduct = product;
            titleText.setText(currentProduct.title);
            descriptionText.setText(currentProduct.description);
        }, t -> Log.e(TAG, "Failed to get products", t));
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveProduct();
        outState.putLong("ID", id);
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
