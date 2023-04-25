package com.myfarm.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AnimalType.class}, version = 1, exportSchema = false)
public abstract class AnimalTypeDatabase extends RoomDatabase {
    public abstract AnimalTypeDao animalTypeDao();

    private static volatile AnimalTypeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AnimalTypeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AnimalTypeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AnimalTypeDatabase.class, "animalType-database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
