package com.myfarm.db;

import android.app.Application;

import java.util.List;

public class ProductRepository {
    private final ProductDao tProductDao;


    ProductRepository(Application application) {
        ProductDatabase db = ProductDatabase.getDatabase(application);
        tProductDao = db.productDao();
    }

    public List<Product> getAllProducts(){
        return tProductDao.getAllProducts();
    }
}
