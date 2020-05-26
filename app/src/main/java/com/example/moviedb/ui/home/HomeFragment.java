package com.example.moviedb.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.R;
import com.example.moviedb.model.ListItem;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView movieList;
    private MovieListAdapter movieListAdapter;
    private List<ListItem> itemList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        movieList = root.findViewById(R.id.movie_list);
        movieListAdapter = new MovieListAdapter(getContext());
        movieList.setAdapter(movieListAdapter);
        movieList.setLayoutManager(new LinearLayoutManager(getActivity()));;
        homeViewModel.getMovieList().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> listItems) {
                movieListAdapter.setMovieList(listItems);
                itemList = listItems;
            }
        });
        return root;
    }
}