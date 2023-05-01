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

    @NonNull
    @ColumnInfo(name = "normalProduction")
    private int normalProduction;

    @ColumnInfo(name = "photoName")
    private String photoName;


    public AnimalType(String animalTypeName, String pregnancyPeriod, int normalProduction, String photoName){
        this.animalTypeName = animalTypeName;
        this.pregnancyPeriod = pregnancyPeriod;
        this.normalProduction = normalProduction;
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

    public int getNormalProduction() {
        return normalProduction;
    }

    public void setNormalProduction(int normalProduction) {
        this.normalProduction = normalProduction;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
