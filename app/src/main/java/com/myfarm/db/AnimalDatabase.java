package com.myfarm.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Animal.class, AnimalType.class, Pregnancy.class}, version = 1, exportSchema = false)
public abstract class AnimalDatabase extends RoomDatabase {
    public abstract AnimalDao animalDao();
    private static volatile AnimalDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AnimalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AnimalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AnimalDatabase.class, "word_database")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}

