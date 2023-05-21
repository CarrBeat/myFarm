package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = "animalType")
public class AnimalType {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "idAnimalType")
    private int idAnimalType;

    @NonNull
    @ColumnInfo(name = "animalTypeName")
    private String animalTypeName;

    @ColumnInfo(name = "pregnancyPeriod")
    private String pregnancyPeriod;


    @ColumnInfo(name = "photoName")
    private String photoName;


    public AnimalType(String animalTypeName, String pregnancyPeriod, String photoName){
        this.animalTypeName = animalTypeName;
        this.pregnancyPeriod = pregnancyPeriod;
        this.photoName = photoName;
    }

    public int getIdAnimalType() {
        return idAnimalType;
    }

    public void setIdAnimalType(int idAnimalType) {
        this.idAnimalType = idAnimalType;
    }

    public String getAnimalTypeName() {
        return animalTypeName;
    }

    public void setAnimalTypeName(String animalTypeName) {
        this.animalTypeName = animalTypeName;
    }

    public String getPregnancyPeriod() {
        return pregnancyPeriod;
    }

    public void setPregnancyPeriod(String pregnancyPeriod) {
        this.pregnancyPeriod = pregnancyPeriod;
    }


    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
