package com.example.moviedb.ui.saved;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.example.moviedb.model.SavedItem;
import com.example.moviedb.ui.home.MovieListAdapter;

import java.util.List;

public class SavedFragment extends Fragment {

    private Context context;
    private SavedViewModel savedViewModel;
    private SavedListAdapter savedListAdapter;
    private RecyclerView savedRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        context = getContext();
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        savedRecyclerView = root.findViewById(R.id.saved_list);
        savedListAdapter = new SavedListAdapter(getContext());
        savedRecyclerView.setAdapter(savedListAdapter);
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedViewModel.getSavedItems().observe(this, new Observer<List<SavedItem>>() {
            @Override
            public void onChanged(List<SavedItem> savedItems) {
                savedListAdapter.setMovieList(savedItems);
            }
        });
        setHasOptionsMenu(false);
        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }
}