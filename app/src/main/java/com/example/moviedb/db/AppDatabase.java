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

@Database(entities = {ListItem.class, SavedItem.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    private static Context mContext;
    private static boolean alreadyFetched = false;

    public abstract ListItemDao listItemDao();
    public abstract SavedItemDao savedItemDao();

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

    public void makeInitialRequest(final String typeOfRequest) {
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
//                            SavedItem savedItem = new SavedItem(uid, title, description, rating, imageURL, type);
//                            SaveItemAsyncTask saveItemAsyncTask = new SaveItemAsyncTask(savedItem);
//                            saveItemAsyncTask.execute();
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
//                        itemsList.add(newItem);
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

    public class SaveItemAsyncTask extends AsyncTask<Void, Void, Void> {
        private SavedItem savedItem;

        public SaveItemAsyncTask(SavedItem savedItem) {
            this.savedItem = savedItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            savedItemDao().insertSavedItem(savedItem);
            return null;
        }
    }

    public class ClearDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            listItemDao().clearTable();
            return null;
        }
    }
}
