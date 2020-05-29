package com.example.moviedb.ui.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedb.R;
import com.example.moviedb.model.ListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView searchResultsView;
    private SearchListAdapter movieListAdapter;

    ArrayList<ListItem> searchResults;
    private String queryString;
    private int pageToLoad = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        searchResults = new ArrayList<>();
        setContentView(R.layout.recycler_view);
        searchResultsView = findViewById(R.id.items_view);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryString = intent.getStringExtra(SearchManager.QUERY);
            searchRequest(queryString, 1);
            movieListAdapter = new SearchListAdapter(this, searchResults);
            searchResultsView.setAdapter(movieListAdapter);
            searchResultsView.setLayoutManager(new LinearLayoutManager(this));

            Log.d("Search", queryString);
            setTitle("Results for:  " + "\"" + queryString + "\"");
        }
        searchResultsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)) {
                    loadNextPage();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchRequest(String queryString, final int nextPageToLoad) {
        queryString = queryString.replaceAll(" ", "%20");
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = String.format("https://api.themoviedb.org/3/search/multi?api_key=900fc2bf9aca7123813b54fd5d90a302&language=en-US&query=%s&page=%s&include_adult=false", queryString, Integer.toString(nextPageToLoad));
        final int originalListSize = searchResults.size();
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
                            searchResults.add(newItem);
                        }
                    }
                    if(searchResults.size() > originalListSize) {
                        movieListAdapter.setDataChange(searchResults);
                        pageToLoad++;
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
    }

    private void loadNextPage() {
        Log.d("Next page to load", "" + pageToLoad);
        searchRequest(queryString, pageToLoad);
    }
}
