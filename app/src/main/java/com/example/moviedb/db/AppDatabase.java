package com.example.moviedb.db;

import com.example.moviedb.model.ListItem;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ListItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
}
