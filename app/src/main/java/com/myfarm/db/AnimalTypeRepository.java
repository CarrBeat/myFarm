package com.myfarm.db;

import android.app.Application;

import java.util.List;

public class AnimalTypeRepository {
    private AnimalTypeDao tAnimalTypeDao;
    private List<AnimalType> AnimalTypes;

    AnimalTypeRepository(Application application){
        AnimalTypeDatabase db = AnimalTypeDatabase.getDatabase(application);
        tAnimalTypeDao = db.animalTypeDao();
        AnimalTypes = tAnimalTypeDao.getAllAnimalTypes();
    }
}
