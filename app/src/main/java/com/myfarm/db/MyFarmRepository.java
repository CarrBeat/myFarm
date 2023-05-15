package com.myfarm.db;

import android.app.Application;

import java.util.List;

public class MyFarmRepository {
    private final AnimalDao tAnimalDao;


    MyFarmRepository(Application application) {
        MyFarmDatabase db = MyFarmDatabase.getDatabase(application);
        tAnimalDao = db.animalDao();
    }

    public List<Animal> getAllAnimals(){
        return (List<Animal>) tAnimalDao.getAllAnimals();
    }
}
