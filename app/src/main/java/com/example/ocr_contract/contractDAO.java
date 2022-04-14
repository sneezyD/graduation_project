package com.example.ocr_contract;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface contractDAO    {
    @Query("SELECT * FROM contract")
    List<Contract> getAll();

    @Query("SELECT * FROM contract WHERE phoneNumber LIKE :phoneNumber")
    Contract findbyphoneNumber(String phoneNumber);

    @Insert
    void insertAll(Contract... contracts);

    @Delete
    void delete(Contract contract);

    @Query("SELECT * FROM contract WHERE id IN (:Ids)")
    List<Contract> loadAllByIds(int[] Ids);
}
