package com.myfarm.db;

import android.app.Application;

import java.lang.reflect.Array;
import java.util.List;

public class AnimalRepository {
    private final AnimalDao tAnimalDao;


    AnimalRepository(Application application) {
        AnimalDatabase db = AnimalDatabase.getDatabase(application);
        tAnimalDao = db.animalDao();
    }

    public List<Animal> getAllAnimals(){
        return (List<Animal>) tAnimalDao.getAllAnimals();
    }
}
