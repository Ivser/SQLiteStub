package com.github.ivser.sqlitestub.model.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * sqliteTest
 * <p>
 * Created by SIvanov on 07.07.2017.
 */

@Entity
public class Product {

    @PrimaryKey
    public final long id;
    public String title;
    public String description;

    public Product(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static ProductBuilder builder(){
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private long id;
        private String title = "";
        private String description = "";

        public ProductBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ProductBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Product build() {
            return new Product(id, title, description);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
