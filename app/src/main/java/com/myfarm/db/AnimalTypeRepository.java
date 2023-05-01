package com.myfarm.db;

import android.app.Application;

import java.util.Collection;

public class AnimalTypeRepository {
    private final AnimalTypeDao tAnimalTypeDao;


    AnimalTypeRepository(Application application){
        AnimalTypeDatabase db = AnimalTypeDatabase.getDatabase(application);
        tAnimalTypeDao = db.animalTypeDao();
    }


    public Collection<? extends String> getAllAnimalTypes() { return tAnimalTypeDao.getAnimalTypeNames(); }
}
