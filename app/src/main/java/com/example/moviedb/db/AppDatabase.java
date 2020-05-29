package com.example.moviedb.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedb.model.ListItem;
import com.example.moviedb.model.ListItemDao;
import com.example.moviedb.model.SavedItem;
import com.example.moviedb.model.SavedItemDao;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Database class following the Room Persistence Library architecture.
 * Holds a single instance of the database and API requests to populate the DB
 */
@Database(entities = {ListItem.class, SavedItem.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //Singular instance of the database
    private static AppDatabase INSTANCE;

    private static Context mContext;
    private static boolean alreadyFetched = false;

    //Abstract declaration of each Dao
    public abstract ListItemDao listItemDao();
    public abstract SavedItemDao savedItemDao();

    //Static method allowing access to the database from anywhere in the app
    public static AppDatabase getInstance(Context context) {
        mContext = context;
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "database.db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Initial request to populate the ListItem table
     * @param typeOfRequest param that signifies the type of request needed
     */
    public void makeInitialRequest(final String typeOfRequest) {
        //Boolean to check if the initial request has already been executed, prevents refetching upon
        //returning to the fragment
        if(!alreadyFetched) {
            ClearDatabaseAsyncTask clearDatabaseAsyncTask = new ClearDatabaseAsyncTask();
            clearDatabaseAsyncTask.execute();

            RequestQueue queue = Volley.newRequestQueue(mContext);
            String URL = String.format("https://api.themoviedb.org/3/%s?api_key=900fc2bf9aca7123813b54fd5d90a302&language=en-US&page=1", typeOfRequest);
            StringRequest stringRequest= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        JSONArray jsonArray = JSONResponse.getJSONArray("results");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject currentObject = (JSONObject) jsonArray.get(i);

                            String uid = currentObject.getString("id");
                            String title;

                            //MovieDB has different keys for the title depending on if its a movie or tv show
                            try {
                                title = currentObject.getString("title");
                            } catch(Exception e) {
                                title = currentObject.getString("name");
                            }
                            Log.d("Hey", title);
                            String description = currentObject.getString("overview");
                            String rating = currentObject.getString("vote_average");
                            String imageURL = currentObject.getString("poster_path");
                            String type = typeOfRequest.substring(0,1);

                            ListItem newItem =  new ListItem(uid, title, description, rating, imageURL, type);
                            InsertItemAsyncTask insertItemAsyncTask = new InsertItemAsyncTask(newItem);
                            insertItemAsyncTask.execute();
                        }
                        alreadyFetched = true;
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
    }

    /**
     * Method that handles all API requests after the initial one
     * particularly for the filter switches
     * @param typeOfRequest
     * @param page integer of the page being requested
     */
    public void makeAPIRequest(final String typeOfRequest, int page) {
        if(page == 1) {
            ClearDatabaseAsyncTask clearDatabaseAsyncTask = new ClearDatabaseAsyncTask();
            clearDatabaseAsyncTask.execute();
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String URL = String.format("https://api.themoviedb.org/3/%s?api_key=900fc2bf9aca7123813b54fd5d90a302&language=en-US&page=%s", typeOfRequest, Integer.toString(page));
        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JSONResponse = new JSONObject(response);
                    JSONArray jsonArray = JSONResponse.getJSONArray("results");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentObject = (JSONObject) jsonArray.get(i);
                        String uid = currentObject.getString("id");
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
                        String type = typeOfRequest.substring(0,1);

                        ListItem newItem =  new ListItem(uid, title, description, rating, imageURL, type);
                        InsertItemAsyncTask insertItemAsyncTask = new InsertItemAsyncTask(newItem);
                        insertItemAsyncTask.execute();
                    }
                    alreadyFetched = true;
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

    //AsyncTask to insert a ListItem into the ListItem table.
    //Access to database cannot be done on the main thread
    public class InsertItemAsyncTask extends AsyncTask<Void, Void, Void> {
        private ListItem listItem;

        public InsertItemAsyncTask(ListItem listItem) {
            this.listItem = listItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listItemDao().insertListItem(listItem);
            return null;
        }
    }

    //AsyncTask to clear the ListItem table. Makes sure that the view will be empty
    //before filling it with new items. Otherwise ListItems will persist on app
    //restarts and filter changes
    public class ClearDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            listItemDao().clearTable();
            return null;
        }
    }
}
