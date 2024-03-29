package com.myfarm.db;

import android.arch.lifecycle.LiveData;

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

    @Query("SELECT * FROM animal")
    LiveData<List<Animal>> getAllAnimals();



}
