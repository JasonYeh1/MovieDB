package com.example.moviedb.ui.home;

import android.app.Application;
import android.content.Context;

import com.example.moviedb.model.ListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<ListItem>> movieList;
    private HomeRepo homeRepo;
    private Context context;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        context = application;
        homeRepo = new HomeRepo(context);
        movieList = homeRepo.getListItems();
    }

    public LiveData<List<ListItem>> getListItems() {
        return movieList;
    }
//
//    public void makeAPIRequest(String typeOfRequest) {
//        homeRepo.makeAPIRequest(typeOfRequest);
//    }
}