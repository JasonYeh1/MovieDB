package com.example.moviedb.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.R;
import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.ListItem;

import java.util.List;

public class HomeFragment extends Fragment {

    private Context context;
    private HomeViewModel homeViewModel;
    private HomeRepo homeRepo;
    private RecyclerView movieList;
    private MovieListAdapter movieListAdapter;
    private Button changeButton;
    private List<ListItem> itemList;
    private String currentListType = "movie/popular";

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        homeRepo = new HomeRepo(context);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        AppDatabase.getInstance(context).makeInitialRequest("movie/popular");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        changeButton = root.findViewById(R.id.button);
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

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.getInstance(context).changeAPIRequest("movie/top_rated");
            }
        });
        return root;
    }
}