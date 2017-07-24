package com.github.ivser.sqlitestub.data;

import com.github.ivser.sqlitestub.model.room.Product;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class RoomProductGenerator {

    private static RoomProductGenerator instance;
    private int n = 0;

    public static RoomProductGenerator getInstance() {
        if (instance == null) {
            instance = new RoomProductGenerator();
        }
        return instance;
    }

    private RoomProductGenerator() {

    }

    public Product next() {
        Product product = Product.builder()
                .setId(n)
                .setTitle("Product" + n)
                .setDescription("Product " + n + " description")
                .build();
        n++;
        return product;
    }
}
