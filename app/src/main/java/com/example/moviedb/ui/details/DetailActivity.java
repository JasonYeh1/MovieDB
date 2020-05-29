package com.example.moviedb.ui.details;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedb.R;
import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.SavedItem;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that appears when a user clicks on an item in any RecyclerView.
 * Provides more detail about the item, and allows users to save the item
 */
public class DetailActivity extends AppCompatActivity {
    //View declarations
    private TextView titleView;
    private ImageView posterView;
    private TextView ratingView;
    private TextView typeView;
    private TextView descriptionView;
    private TextView releaseView;
    private TextView statusView;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Setting view associations with the XML
        setContentView(R.layout.activity_details);
        titleView = findViewById(R.id.title_view);
        posterView = findViewById(R.id.movie_poster);
        ratingView = findViewById(R.id.item_rating);
        typeView = findViewById(R.id.item_type);
        descriptionView = findViewById(R.id.item_description);
        releaseView = findViewById(R.id.release_date);
        statusView = findViewById(R.id.status);
        saveButton = findViewById(R.id.save_button);

        //Retrieving Intent extras from calling class
        boolean noSave = getIntent().getBooleanExtra("saveButton", true);
        final String uid = getIntent().getStringExtra("uid");
        final String title = getIntent().getStringExtra("title");
        final String description = getIntent().getStringExtra("description");
        final String rating = getIntent().getStringExtra("rating");
        final String imageURL = getIntent().getStringExtra("imageURL");
        final String type = getIntent().getStringExtra("type");
        getDetails(uid, type);

        titleView.setText(title);
        if(!imageURL.equals("null")) {
            Picasso.get().load("https://image.tmdb.org/t/p/w300" + imageURL).into(posterView);
        }
        ratingView.setText(rating +"/10");
        if(type.equals("m")) {
            typeView.setText("Movie");
        } else {
            typeView.setText("TV Show");
        }
        descriptionView.setText(description);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(noSave) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DetailActivity.this, "Saved To List", Toast.LENGTH_SHORT).show();
                    SavedItem newSavedItem = new SavedItem(uid, title, description, rating, imageURL, type);
                    AppDatabase.getInstance(getApplicationContext()).savedItemDao().insertSavedItem(newSavedItem);
                }
            });
        } else {
            saveButton.setVisibility(View.GONE);
        }
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

    /**
     * Method to make an API request to retrieve more detail about the item
     * @param uid   uniqueId provided by MovieDB
     * @param type  type of entertainment being requested (movie or tv)
     */
    private void getDetails(String uid, String type) {
        final String dateKey;

        //MovieDB has different JSON keys for similar attributes
        if(type.equals("m")) {
            type = "movie";
            dateKey = "release_date";
        } else {
            type = "tv";
            dateKey = "first_air_date";
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = String.format("https://api.themoviedb.org/3/%s/%s?api_key=900fc2bf9aca7123813b54fd5d90a302&language=en-US", type, uid);
        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject currentObject = new JSONObject(response);
                    //Setting the text here because Volley is asynchronous
                    releaseView.setText("Release Date: " + currentObject.getString(dateKey));
                    statusView.setText("Status: " + currentObject.getString("status"));
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
