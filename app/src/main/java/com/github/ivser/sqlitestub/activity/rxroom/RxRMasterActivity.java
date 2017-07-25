package com.github.ivser.sqlitestub.activity.rxroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.ivser.sqlitestub.R;
import com.github.ivser.sqlitestub.data.RoomProductGenerator;
import com.github.ivser.sqlitestub.db.rxroom.RxRoomDb;
import com.github.ivser.sqlitestub.model.room.Product;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 24.07.2017.
 */

public class RxRMasterActivity extends Activity {

    private static final String TAG = RxRMasterActivity.class.getName();

    private RxRoomDb database;
    private ArrayAdapter<String> adapter;
    private List<Product> products;
    private List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ListView list = (ListView) findViewById(R.id.data);
        database = RxRoomDb.getDatabase(getApplicationContext());

        readData();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.title, titles);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(RxRMasterActivity.this, RxRDetailActivity.class);
            i.putExtra("ID", products.get(position).id);
            startActivity(i);
        });
    }

    private void readData() {
        database.productModel().getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allproducts -> {
                    titles.clear();
                    RxRMasterActivity.this.products = allproducts;
                    for (Product product : allproducts) {
                        titles.add(product.title);
                    }
                    adapter.notifyDataSetChanged();
                }, t -> Log.e(TAG, "Failed to get products", t));
    }

    @Override
    protected void onDestroy() {
        RxRoomDb.destroyInstance();
        super.onDestroy();

    }

    public void onAddClick(View view) {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            database.productModel().addProduct(RoomProductGenerator.getInstance().next());
            e.onNext(true);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(b -> {
                readData();
                adapter.notifyDataSetChanged();
            });
    }

    public void onRemoveLastClick(View view) {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            database.productModel().removeroduct(products.get(products.size()-1));
            e.onNext(true);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(b -> {
                readData();
                adapter.notifyDataSetChanged();
            });
    }

}
