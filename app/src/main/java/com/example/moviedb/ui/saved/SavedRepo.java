package com.example.moviedb.ui.saved;

import android.content.Context;

import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.SavedItem;
import com.example.moviedb.model.SavedItemDao;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Repository for the SavedFragment
 */
public class SavedRepo {

    private SavedItemDao savedItemDao;
    private AppDatabase appDB;
    private Context context;
    private LiveData<List<SavedItem>> savedList;

    public SavedRepo(Context context) {
        this.context = context;
        appDB = AppDatabase.getInstance(context);
        savedItemDao = appDB.savedItemDao();
        savedList = savedItemDao.getSavedItems();
    }

    public LiveData<List<SavedItem>> getSavedItems() {
        return savedList;
    }


}
