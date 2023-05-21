package com.myfarm.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Animal.class, AnimalType.class, Pregnancy.class,
        Statistics.class}, version = 1, exportSchema = false)
public abstract class MyFarmDatabase extends RoomDatabase {
    public abstract AnimalDao animalDao();
    public abstract AnimalTypeDao animalTypeDao();
    public abstract PregnancyDao pregnancyDao();
    public abstract StatisticsDao statisticsDao();

    private static volatile MyFarmDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MyFarmDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyFarmDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MyFarmDatabase.class, "my_farm_database")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}

