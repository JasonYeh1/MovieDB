package com.example.moviedb.ui.home;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedb.model.ListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class HomeRepo {

    public static MutableLiveData<List<ListItem>> getMovieList(Context context) {
        final MutableLiveData<List<ListItem>> movieList = new MutableLiveData<>();
        final ArrayList<ListItem> itemsList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = "https://api.themoviedb.org/3/movie/popular?api_key=900fc2bf9aca7123813b54fd5d90a302&language=en-US&page=1";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JSONResponse = new JSONObject(response);
                    JSONArray jsonArray = JSONResponse.getJSONArray("results");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentObject = (JSONObject) jsonArray.get(i);
                        String title = currentObject.getString("title");
                        String description = currentObject.getString("overview");
                        String rating = currentObject.getString("vote_average");
                        String imageURL = currentObject.getString("poster_path");
                        String type = "movie";
                        ListItem newItem =  new ListItem(title, description, rating, imageURL, type);
                        itemsList.add(newItem);
                    }
                    movieList.setValue(itemsList);
//                    Log.d("HEY", JSONResponse.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);
        return movieList;
    }
}
