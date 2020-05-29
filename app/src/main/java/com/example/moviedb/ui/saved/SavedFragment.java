package com.example.moviedb.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviedb.R;
import com.example.moviedb.model.SavedItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Fragment class for displaying the items the user has saved
 */
public class SavedFragment extends Fragment {

    private SavedViewModel savedViewModel;
    private SavedListAdapter savedListAdapter;
    private RecyclerView savedRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        savedViewModel = ViewModelProviders.of(this).get(SavedViewModel.class);
        View root = inflater.inflate(R.layout.recycler_view, container, false);
        savedRecyclerView = root.findViewById(R.id.items_view);
        savedListAdapter = new SavedListAdapter(getContext());
        savedRecyclerView.setAdapter(savedListAdapter);
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Observer to keep track of the LiveData, will update UI when the list changes
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
        menu.clear();   //Disable the filter button in the saved fragment
        super.onPrepareOptionsMenu(menu);
    }
}