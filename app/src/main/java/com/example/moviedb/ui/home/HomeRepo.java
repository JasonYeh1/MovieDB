package com.example.moviedb.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.ListItem;
import com.example.moviedb.model.ListItemDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

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

    public LiveData<List<ListItem>> getListItems() {
        return movieList;
    }


}
