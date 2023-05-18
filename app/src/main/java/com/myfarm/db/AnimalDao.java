package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AnimalDao {
    @Insert
    void insertAll(Animal... animals);

    @Delete
    void delete(Animal animal);

    @Query("SELECT * FROM animal")
    List<Animal> getAllAnimals();

    @Query("SELECT animalTypeName from animalType where idAnimalType = :idAnimalType")
    String getAnimalTypeName(int idAnimalType);

    @Update
    void updateAnimal(Animal animal);

}
