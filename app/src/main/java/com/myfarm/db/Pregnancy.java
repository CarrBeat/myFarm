package com.myfarm.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

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

    @ColumnInfo(name = "notify")
    private Boolean notify;

    public Pregnancy(String approximatelyChildbirth, Boolean notify){
        this.approximatelyChildbirth = approximatelyChildbirth;
        this.notify = notify;
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

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }
}
