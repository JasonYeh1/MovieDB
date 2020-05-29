package com.example.moviedb.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
/**
 * DAO interface providing methods to access and query the SavedItem table
 */
public interface SavedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSavedItem(SavedItem savedItem);

    /**
     * Method to get the list of SavedItems
     * @return LiveData list of SavedItems
     */
    @Query("SELECT * from SavedItem")
    LiveData<List<SavedItem>> getSavedItems();

    /**
     * Database method to delete a single entry in the SavedItem table
     * @param uniqueId primary key needed to deleted correct entry
     */
    @Query("DELETE FROM SavedItem WHERE uid = :uniqueId")
    void deleteSavedItem(String uniqueId);
}
