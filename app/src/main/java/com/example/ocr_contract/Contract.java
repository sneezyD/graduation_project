package com.example.ocr_contract;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.io.Serializable;
import java.util.List;

@Entity
public class Contract implements Serializable {
    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="address")
    public String address;

    @ColumnInfo(name="contractDate")
    public String contractDate;

    @ColumnInfo(name="workingDate")
    public String workingDate;

    /*
    @ColumnInfo(name="phoneNumber")
    public String phoneNumber;
     */

    @ColumnInfo(name="sum")
    public String sum;

    @ColumnInfo(name="measurementDate")
    public String measurementDate;

    @ColumnInfo(name="boundingPoly")
    public String boundingPoly;

    @ColumnInfo(name="picturePath")
    public String picturePath;

    @ColumnInfo(name="changes")
    public String changes;

    @PrimaryKey
    @NonNull
    public String phoneNumber;
}