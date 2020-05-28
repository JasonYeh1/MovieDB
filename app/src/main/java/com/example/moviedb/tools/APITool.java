package com.example.moviedb.tools;

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

public class APITool {

    private static ArrayList<ListItem> resultList = new ArrayList<>();

    public static ArrayList<ListItem> searchRequest(Context context, String queryString) {
        Log.d("SearchRequst", "IM BEING CALLED");
        resultList.clear();
        queryString = queryString.replaceAll(" ", "%20");
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = String.format("https://api.themoviedb.org/3/search/multi?api_key=900fc2bf9aca7123813b54fd5d90a302&language=en-US&query=%s&page=1&include_adult=false", queryString);
        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JSONResponse = new JSONObject(response);
                    JSONArray jsonArray = JSONResponse.getJSONArray("results");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentObject = (JSONObject) jsonArray.get(i);
                        String uid = currentObject.getString("id");
                        String type = currentObject.getString("media_type");
                        if(type.equals("movie") || type.equals("tv")) {
                            String title;
                            try {
                                title = currentObject.getString("title");
                            } catch(Exception e) {
                                title = currentObject.getString("name");
                            }
                            Log.d("Hey", title);
                            String description = currentObject.getString("overview");
                            String rating = currentObject.getString("vote_average");
                            String imageURL = currentObject.getString("poster_path");
                            type = type.substring(0,1);
                            ListItem newItem =  new ListItem(uid, title, description, rating, imageURL, type);
                            resultList.add(newItem);
                        }
                    }
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
        Log.d("HELLO", "" + resultList.size());
        return resultList;
    }
}
