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

    @Update
    void updateAnimal(Animal animal);

    @Query("SELECT * FROM animal")
    List<Animal> getAllAnimals();

    @Query("SELECT animalTypeName from animalType where idAnimalType = :idAnimalType")
    String getAnimalTypeName(int idAnimalType);

    @Query("SELECT idAnimal from animal where pregnancyID = :idPregnancy")
    int getAnimalIDByPregnancy(int idPregnancy);

    @Query("SELECT animalName from animal where idAnimal = :animalID")
    String getAnimalName(int animalID);

    @Query("SELECT animalTypeName from animalType where idAnimalType = " +
            "(select animalTypeID from animal where idAnimal = :animalID)")
    String getAnimalTypeNameByAnimalID(int animalID);

    @Query("SELECT * from animal where pregnancyID = :idPregnancy")
    Animal getAnimalByPregnancy(int idPregnancy);

}
