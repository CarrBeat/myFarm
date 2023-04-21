package com.myfarm.db;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface PregnancyDao {
    @Insert
    void insertAll(Pregnancy... pregnancies);

    @Delete
    void delete(Pregnancy pregnancy);

    @Query("SELECT * FROM pregnancy")
    List<Pregnancy> getAllPregnancies();
}
