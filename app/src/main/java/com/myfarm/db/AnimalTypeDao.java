package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AnimalTypeDao {
    @Insert
    void insertAll(AnimalType... animalTypes);

    @Delete
    void delete(AnimalType animalType);

    @Query("SELECT * FROM animalType")
    List<AnimalType> getAllAnimalTypes();

}
