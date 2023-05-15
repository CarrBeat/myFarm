package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = "productivity")
public class Productivity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idProductivity")
    private int idProductivity;

    @NonNull
    @ColumnInfo(name = "productID", index = true)
    private int productID;

    @NonNull
    @ColumnInfo(name = "animalID", index = true)
    private int animalID;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    public Productivity(int productID, int animalID, String date){
        this.productID = productID;
        this.animalID = animalID;
        this.date = date;
    }

    public int getIdProductivity() {
        return idProductivity;
    }

    public void setIdProductivity(int idProductivity) {
        this.idProductivity = idProductivity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
