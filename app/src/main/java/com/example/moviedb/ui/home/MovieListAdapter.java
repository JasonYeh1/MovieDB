package com.example.moviedb.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviedb.R;
import com.example.moviedb.model.ListItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<ListItem> itemList;

    public MovieListAdapter(Context context) {
        super();
        itemList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        ItemListViewHolder vh = new ItemListViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItem listItem = itemList.get(position);

        ((ItemListViewHolder)holder).title.setText(listItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setMovieList(List<ListItem> movieList) {
        itemList = movieList;
        notifyDataSetChanged();
    }

    private class ItemListViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView poster;
        private TextView title;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            poster = itemView.findViewById(R.id.movie_poster);
            title = itemView.findViewById(R.id.movie_title);
        }
    }
}
