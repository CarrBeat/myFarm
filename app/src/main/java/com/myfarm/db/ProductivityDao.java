package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProductivityDao {
    @Insert
    void insertAll(Productivity... productivities);

    @Delete
    void delete(Productivity productivity);

    @Query("SELECT * FROM productivity")
    List<Productivity> getAllProductivities();
}
