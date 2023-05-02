package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AnimalDao {
    @Insert
    void insertAll(Animal... animals);

    @Delete
    void delete(Animal animal);

    @Query("SELECT animalName FROM animal")
    List<String> getAllAnimals();



}
