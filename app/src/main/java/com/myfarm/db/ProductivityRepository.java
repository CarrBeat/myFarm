package com.myfarm.db;

import android.app.Application;

import java.util.List;

public class ProductivityRepository {
    private final ProductivityDao tProductivityDao;


    ProductivityRepository(Application application) {
        ProductivityDatabase db = ProductivityDatabase.getDatabase(application);
        tProductivityDao = db.productivityDao();
    }

    public List<Productivity> getAllProductivities(){
        return tProductivityDao.getAllProductivities();
    }
}
