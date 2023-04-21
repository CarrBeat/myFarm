package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = "product")
public class Product {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idProduct")
    private int idProduct;

    @NonNull
    @ColumnInfo(name = "productName")
    private String productName;

    public Product(String productName){
        this.productName = productName;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
