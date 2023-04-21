package com.myfarm.db;

import android.app.Application;

import java.util.List;

public class AnimalRepository {
    private AnimalDao tAnimalDao;
    private List<Animal> AnimalObjects;


    AnimalRepository(Application application) {
        AnimalDatabase db = AnimalDatabase.getDatabase(application);
        tAnimalDao = db.animalDao();
        AnimalObjects = tAnimalDao.getAllAnimals();
    }


}
