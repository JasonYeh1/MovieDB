package com.example.moviedb.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ListItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListItem(ListItem listItem);

    @Query("SELECT * from ListItem")
    LiveData<List<ListItem>> getListItems();
}
