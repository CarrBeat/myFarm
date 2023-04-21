package com.myfarm.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ProductivityDatabase.class}, version = 1, exportSchema = false)
public abstract class ProductivityDatabase extends RoomDatabase {
    public abstract ProductivityDao productivityDao();

    private static volatile ProductivityDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ProductivityDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductivityDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ProductivityDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

