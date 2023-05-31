package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PregnancyDao {
    @Insert
    long insert(Pregnancy pregnancy);

    @Delete
    void delete(Pregnancy pregnancy);

    @Query("SELECT * FROM pregnancy")
    List<Pregnancy> getAllPregnancies();

    @Query("delete from pregnancy where idPregnancy = :pregnancyId")
    void deletePregnancyById(int pregnancyId);

    @Query("select approximatelyChildbirth from pregnancy where idPregnancy = :pregnancyId")
    String getChildbirthDateByPregnancyID(int pregnancyId);

    @Query("select notifyID from pregnancy where idPregnancy = :pregnancyID")
    int getNotifyIDByPregnancyID(int pregnancyID);
}
