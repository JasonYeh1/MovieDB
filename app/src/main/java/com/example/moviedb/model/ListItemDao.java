package com.example.moviedb.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
/**
 * DAO interface providing methods to access and query the ListItem table
 */
public interface ListItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListItem(ListItem listItem);

    /**
     *
     * @return current list of ListItems
     */
    @Query("SELECT * from ListItem")
    LiveData<List<ListItem>> getListItems();

    /**
     * Method to clear ListItem table
     */
    @Query("DELETE from ListItem")
    void clearTable();
}
