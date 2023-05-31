package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = "pregnancy")
public class Pregnancy {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "idPregnancy")
    private int idPregnancy;

    @NonNull
    @ColumnInfo(name = "approximatelyChildbirth")
    private String approximatelyChildbirth;

    @ColumnInfo(name = "notifyID")
    private long notifyID;

    public Pregnancy(String approximatelyChildbirth, long notifyID){
        this.approximatelyChildbirth = approximatelyChildbirth;
        this.notifyID = notifyID;
    }

    public int getIdPregnancy() {
        return idPregnancy;
    }

    public void setIdPregnancy(int idPregnancy) {
        this.idPregnancy = idPregnancy;
    }

    public String getApproximatelyChildbirth() {
        return approximatelyChildbirth;
    }

    public void setApproximatelyChildbirth(String approximatelyChildbirth) {
        this.approximatelyChildbirth = approximatelyChildbirth;
    }

    public long getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(long notifyID) {
        this.notifyID = notifyID;
    }
}
