package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import io.reactivex.rxjava3.annotations.NonNull;

@Entity(foreignKeys = {@ForeignKey(entity = AnimalType.class, parentColumns = "idAnimalType",
        childColumns = "animalTypeID"), @ForeignKey(entity = Pregnancy.class,
        parentColumns = "idPregnancy", childColumns = "pregnancyID")}, tableName = "animal")

public class Animal {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "idAnimal")
    private int idAnimal;

    @NonNull
    @ColumnInfo(name = "animalName")
    private String animalName;

    @NonNull
    @ColumnInfo(name = "animalTypeID", index = true)
    private int animalTypeID;

    @NonNull
    @ColumnInfo(name = "birthdate")
    private String birthdate;

    @ColumnInfo(name = "pregnancyID")
    private Boolean pregnancyID;

    @NonNull
    @ColumnInfo(name = "female")
    private Boolean female;

    public Animal(String animalName, int animalTypeID, String birthdate, boolean pregnancyID, boolean female){
        this.animalName = animalName;
        this.animalTypeID = animalTypeID;
        this.birthdate = birthdate;
        this.pregnancyID = pregnancyID;
        this.female = female;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public int getAnimalTypeID() {
        return animalTypeID;
    }

    public void setAnimalTypeID(int animalTypeID) {
        this.animalTypeID = animalTypeID;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getPregnancyID() {
        return pregnancyID;
    }

    public void setPregnancyID(Boolean pregnancyID) {
        this.pregnancyID = pregnancyID;
    }

    public Boolean getFemale() {
        return female;
    }

    public void setFemale(Boolean female) {
        this.female = female;
    }
}








