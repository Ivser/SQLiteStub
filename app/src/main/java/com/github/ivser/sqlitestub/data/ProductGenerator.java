package com.github.ivser.sqlitestub.data;

import com.github.ivser.sqlitestub.model.Product;

/**
 * SQLiteStub
 * <p>
 * Created by SIvanov on 21.07.2017.
 */

public class ProductGenerator {

    private static ProductGenerator instance;
    private int n = 0;

    public static ProductGenerator getInstance() {
        if (instance == null) {
            instance = new ProductGenerator();
        }
        return instance;
    }

    private ProductGenerator() {

    }

    public Product next() {
        Product product = new Product();
        product.title = "Product" + n;
        product.description = "Product " + n + " description";
        n++;
        return product;
    }

}
