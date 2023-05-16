package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import java.util.Map;

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


}
