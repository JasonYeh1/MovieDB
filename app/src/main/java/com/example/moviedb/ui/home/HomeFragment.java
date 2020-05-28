package com.example.moviedb.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moviedb.R;
import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.ListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private Context context;
    private HomeViewModel homeViewModel;
    private HomeRepo homeRepo;
    private RecyclerView movieList;
    private MovieListAdapter movieListAdapter;
    private List<ListItem> itemList;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        homeRepo = new HomeRepo(context);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        AppDatabase.getInstance(context).makeInitialRequest("movie/top_rated");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        movieList = root.findViewById(R.id.movie_list);
        movieListAdapter = new MovieListAdapter(getContext());
        movieList.setAdapter(movieListAdapter);
        movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeViewModel.getListItems().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> listItems) {
                movieListAdapter.setMovieList(listItems);
                itemList = listItems;
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.top_rated_movies) {
            AppDatabase.getInstance(context).changeAPIRequest("movie/top_rated");
        } else if(item.getItemId() == R.id.popular_movies) {
            AppDatabase.getInstance(context).changeAPIRequest("movie/popular");
        } else if(item.getItemId() == R.id.now_playing_movies)  {
            AppDatabase.getInstance(context).changeAPIRequest("movie/now_playing");
        } else if(item.getItemId() == R.id.popular_tv) {
            AppDatabase.getInstance(context).changeAPIRequest("tv/popular");
        } else if(item.getItemId() == R.id.top_rated_tv) {
            AppDatabase.getInstance(context).changeAPIRequest("tv/top_rated");
        }
        return super.onOptionsItemSelected(item);
    }
}