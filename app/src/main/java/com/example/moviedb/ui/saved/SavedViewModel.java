package com.example.moviedb.ui.saved;

import android.app.Application;
import android.content.Context;

import com.example.moviedb.model.SavedItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * ViewModel for the SavedFragment
 */
public class SavedViewModel extends AndroidViewModel {

    private LiveData<List<SavedItem>> savedList;
    private Context context;
    private SavedRepo savedRepo;

    public SavedViewModel(@NonNull Application application) {
        super(application);
        context = application;
        savedRepo = new SavedRepo(context);
        savedList = savedRepo.getSavedItems();
    }

    public LiveData<List<SavedItem>> getSavedItems() {
        return savedList;
    }
}