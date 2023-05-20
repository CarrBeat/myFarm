package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PregnancyDao {
    @Insert
    void insertAll(Pregnancy... pregnancies);

    @Delete
    void delete(Pregnancy pregnancy);
/*
    @Query ("SELECT CAST(GETDATE() + (SELECT pregnancyPeriod from animalType, " +
            "animal where animalTypeID = :animalID) AS DATE)")
    String calcChildbirthDate(String startPregnancy, int animalID);*/

    @Query("SELECT * FROM pregnancy")
    List<Pregnancy> getAllPregnancies();
}
