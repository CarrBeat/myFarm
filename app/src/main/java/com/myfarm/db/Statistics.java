package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = "statistics")
public class Statistics {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idStatistics")
    private int idStatistics;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "animalID", index = true)
    private int animalID;

    @NonNull
    @ColumnInfo(name = "weight")
    private float weight;

    public Statistics(String date, int animalID, float weight) {
        this.date = date;
        this.animalID = animalID;
        this.weight = weight;
    }


    public int getIdStatistics() {
        return idStatistics;
    }

    public void setIdStatistics(int idStatistics) {
        this.idStatistics = idStatistics;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
