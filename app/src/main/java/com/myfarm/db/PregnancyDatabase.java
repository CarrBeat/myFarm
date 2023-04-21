package com.myfarm.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Pregnancy.class}, version = 1, exportSchema = false)
public abstract class PregnancyDatabase extends RoomDatabase {
    public abstract PregnancyDao pregnancyDao();

    private static volatile PregnancyDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PregnancyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AnimalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PregnancyDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

