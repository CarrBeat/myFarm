package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface StatisticsDao {
    @Insert
    void insertAll(Statistics... statistics);

    @Delete
    void delete(Statistics statistics);

    @Query("SELECT * FROM Statistics")
    List<Statistics> getAllStatistics();
}
