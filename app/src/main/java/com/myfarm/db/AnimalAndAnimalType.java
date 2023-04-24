package com.myfarm.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AnimalAndAnimalType {
    @Embedded public AnimalType animalType;
    @Relation(
            parentColumn = "idAnimalType",
            entityColumn = "animalTypeID"
    )
    public List<Animal> animalList;
}
