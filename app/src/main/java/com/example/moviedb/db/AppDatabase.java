package com.example.moviedb.db;

import android.content.Context;
import android.os.AsyncTask;
import android.os.strictmode.InstanceCountViolation;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedb.model.ListItem;
import com.example.moviedb.model.ListItemDao;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ListItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    private static Context mContext;
    private static boolean alreadyFetched = false;

    public abstract ListItemDao listItemDao();

    public static AppDatabase getInstance(Context context) {
        mContext = context;
        if(INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().fallbackToDestructiveMigration().build();
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
                            String title = currentObject.getString("title");
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
//                    movieList.setValue(itemsList);
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
        }
    }

    public void changeAPIRequest(final String typeOfRequest) {
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
                        String title = currentObject.getString("title");
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
//                    movieList.setValue(itemsList);
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

    public class ClearDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            INSTANCE.clearAllTables();
            return null;
        }
    }
}
