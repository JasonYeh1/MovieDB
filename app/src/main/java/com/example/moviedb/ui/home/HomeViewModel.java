package com.example.moviedb.ui.home;

import android.app.Application;
import android.content.Context;

import com.example.moviedb.model.ListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<ListItem>> movieList;
    private Context context;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public MutableLiveData<List<ListItem>> getMovieList() {
        movieList = HomeRepo.getMovieList(context);
        return movieList;
    }
}