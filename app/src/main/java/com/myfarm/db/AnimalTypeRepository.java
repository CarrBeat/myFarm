package com.myfarm.db;

import android.app.Application;

import java.util.List;

public class AnimalTypeRepository {
    private final AnimalTypeDao tAnimalTypeDao;


    AnimalTypeRepository(Application application){
        AnimalTypeDatabase db = AnimalTypeDatabase.getDatabase(application);
        tAnimalTypeDao = db.animalTypeDao();
    }


    public List<AnimalType> getAllAnimalTypes() { return tAnimalTypeDao.getAllAnimalTypes(); }
}
