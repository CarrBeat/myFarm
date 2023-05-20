package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AnimalTypeDao {
    @Insert
    void insertAll(AnimalType... animalTypes);

    @Delete
    void delete(AnimalType animalType);

    @Query("SELECT animalTypeName FROM animalType")
    List<String> getAnimalTypeNames();

    @Query("SELECT photoName FROM animalType where animalTypeName = :typeName")
    String getPhotoNameByAnimalTypeName(String typeName);

    @Query("SELECT photoName FROM animalType where idAnimalType = :idAnimalType")
    String getPhotoNameByIDAnimalType(int idAnimalType);

    @Query("SELECT pregnancyPeriod from animalType where idAnimalType = :animalTypeID")
    int getPregnancyPeriodByAnimalTypeID(int animalTypeID);

}
