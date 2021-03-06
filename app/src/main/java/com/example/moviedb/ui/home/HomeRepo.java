package com.example.moviedb.ui.home;

import android.content.Context;

import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.ListItem;
import com.example.moviedb.model.ListItemDao;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Repository for the HomeFragment
 */
public class HomeRepo {

    private ListItemDao listItemDao;
    private AppDatabase appDB;
    private Context context;
    private LiveData<List<ListItem>> movieList;

    public HomeRepo(Context context) {
        this.context = context;
        appDB = AppDatabase.getInstance(context);
        listItemDao = appDB.listItemDao();
        movieList = listItemDao.getListItems();
    }

    /**
     * Method to retrieve the current list of items
     * @return
     */
    public LiveData<List<ListItem>> getListItems() {
        return movieList;
    }


}
