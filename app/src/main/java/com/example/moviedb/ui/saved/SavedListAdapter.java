package com.example.moviedb.ui.saved;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviedb.R;
import com.example.moviedb.model.ListItem;
import com.example.moviedb.model.SavedItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class SavedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<SavedItem> itemList;
    private Context context;

    public SavedListAdapter(Context context) {
        super();
        itemList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.context = context;
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
        SavedItem listItem = itemList.get(position);
        ImageView imageView = ((ItemListViewHolder) holder).poster;
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        String imageURL = listItem.getImageURL();
        if(!imageURL.equals("null")) {
            Picasso.get().load("https://image.tmdb.org/t/p/w300" + listItem.getImageURL()).placeholder(circularProgressDrawable).noFade().into(imageView);
        }
        Log.d("Sizes", "" + imageView.getDrawable().getIntrinsicHeight() + " " + imageView.getDrawable().getIntrinsicHeight());
        ((ItemListViewHolder)holder).title.setText(listItem.getTitle());
        ((ItemListViewHolder)holder).title.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setMovieList(List<SavedItem> movieList) {
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
