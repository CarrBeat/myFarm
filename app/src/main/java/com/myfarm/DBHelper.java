package com.myfarm;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static String DBPath = "app/src/main/assets/myfarm.db";
    private static String DBName = "myfarm.db";
    private static final int SCHEMA = 1;

    static final String TABLE = "animalTypeInfo"; // название таблицы
    static final String column_idAnimalType = "idAnimalType"; // объявляем константы атрибутов
    static final String column_animalTypeName = "animalTypeName";
    static final String column_pregnancyPeriod = "pregnancyPeriod";
    static final String column_averageLifeExpectancy = "averageLifeExpectancy";

    private Context myContext;

    DatabaseHelper(Context context) {
        super(context, DBName, null, SCHEMA);
        this.myContext=context;
        DBPath =context.getFilesDir().getPath() + DBName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    void create_db(){
        File file = new File(DBPath);
        if (!file.exists()) {
            try(InputStream myInput = myContext.getAssets().open(DBName);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DBPath)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DBPath null, SQLiteDatabase.OPEN_READWRITE);
    }
}




















