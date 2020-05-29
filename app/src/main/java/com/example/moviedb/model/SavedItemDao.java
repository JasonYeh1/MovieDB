package com.example.moviedb.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SavedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSavedItem(SavedItem savedItem);

    @Query("SELECT * from SavedItem")
    LiveData<List<SavedItem>> getSavedItems();

    @Query("DELETE FROM SavedItem WHERE uid = :uniqueId")
    void deleteSavedItem(String uniqueId);
}
