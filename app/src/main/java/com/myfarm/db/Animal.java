package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = "animal")
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

    @ColumnInfo(name = "pregnancyID", index = true)
    private int pregnancyID;

    @NonNull
    @ColumnInfo(name = "female")
    private Boolean female;

    @ColumnInfo(name = "weight")
    private float weight;

    public Animal(String animalName, int animalTypeID, String birthdate, int pregnancyID,
                  boolean female, float weight){
        this.animalName = animalName;
        this.animalTypeID = animalTypeID;
        this.birthdate = birthdate;
        this.pregnancyID = pregnancyID;
        this.female = female;
        this.weight = weight;
    }

    @Ignore
    public Animal(String animalName, int animalTypeID, String birthdate, boolean female){
        this.animalName = animalName;
        this.animalTypeID = animalTypeID;
        this.birthdate = birthdate;
        this.female = female;
    }

    @Ignore
    public Animal(String animalName, int animalTypeID, String birthdate, boolean female,
                  float weight){
        this.animalName = animalName;
        this.animalTypeID = animalTypeID;
        this.birthdate = birthdate;
        this.female = female;
        this.weight = weight;
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

    public int getPregnancyID() {
        return pregnancyID;
    }

    public void setPregnancyID(int pregnancyID) {
        this.pregnancyID = pregnancyID;
    }

    public Boolean getFemale() {
        return female;
    }

    public void setFemale(Boolean female) {
        this.female = female;
    }

    public float getWeight() { return weight; }

    public void setWeight(float weight) { this.weight = weight; }
}








