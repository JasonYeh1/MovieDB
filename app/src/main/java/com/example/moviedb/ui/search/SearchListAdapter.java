package com.example.moviedb.ui.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviedb.R;
import com.example.moviedb.model.ListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<ListItem> itemList;

    public SearchListAdapter(Context context, List<ListItem> listItems) {
        super();
        itemList = listItems;
        notifyDataSetChanged();
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
        ImageView imageView = ((ItemListViewHolder) holder).poster;
        String imageURL = listItem.getImageURL();
        if(!imageURL.equals("null")) {
            Picasso.get().load("https://image.tmdb.org/t/p/w300" + listItem.getImageURL()).into(imageView);
        }
        Log.d("Sizes", "" + imageView.getHeight() + " " + imageView.getWidth());
        ((ItemListViewHolder)holder).title.setText(listItem.getTitle());
        ((ItemListViewHolder)holder).title.setVisibility(View.VISIBLE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setDataChange(List<ListItem> searchResults) {
        itemList = searchResults;
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
