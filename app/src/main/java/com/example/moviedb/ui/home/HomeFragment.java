package com.example.moviedb.ui.home;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviedb.R;
import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.ListItem;
import com.example.moviedb.ui.search.SearchResultsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private Context context;
    private HomeViewModel homeViewModel;
    private RecyclerView movieList;
    private MovieListAdapter movieListAdapter;
    private String filter;
    private int nextPageToLoad = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        filter = sharedPreferences.getString("filter", "movie/top_rated");
        AppDatabase.getInstance(context).makeInitialRequest(filter);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        movieList = root.findViewById(R.id.movie_list);
        movieListAdapter = new MovieListAdapter(getContext());
        movieList.setAdapter(movieListAdapter);
        movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeViewModel.getListItems().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> listItems) {
                movieListAdapter.setMovieList(listItems);
            }
        });
        setHasOptionsMenu(true);

        movieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)) {
                    loadNextPage();
                }
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_bar, menu);

        SearchManager searchManager = (SearchManager) this.getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this.getActivity(), SearchResultsActivity.class)));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled (true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.top_rated_movies) {
            filter = "movie/top_rated";
            AppDatabase.getInstance(context).makeAPIRequest(filter, 1);
        } else if(item.getItemId() == R.id.popular_movies) {
            filter = "movie/popular";
            AppDatabase.getInstance(context).makeAPIRequest(filter, 1);
        } else if(item.getItemId() == R.id.now_playing_movies)  {
            filter = "movie/now_playing";
            AppDatabase.getInstance(context).makeAPIRequest(filter, 1);
        } else if(item.getItemId() == R.id.popular_tv) {
            filter = "tv/popular";
            AppDatabase.getInstance(context).makeAPIRequest(filter, 1);
        } else if(item.getItemId() == R.id.top_rated_tv) {
            filter = "tv/top_rated";
            AppDatabase.getInstance(context).makeAPIRequest(filter, 1);
        }
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("filter", filter);
        editor.apply();
        return super.onOptionsItemSelected(item);
    }

    private void loadNextPage() {
        nextPageToLoad += 1;
        Log.d("Next page to load", "" + nextPageToLoad);
        AppDatabase.getInstance(context).makeAPIRequest(filter, nextPageToLoad);
    }
}