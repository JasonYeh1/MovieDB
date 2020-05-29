package com.example.moviedb.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviedb.R;
import com.example.moviedb.model.ListItem;
import com.example.moviedb.ui.details.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<ListItem> itemList;
    private Context context;

    public SearchListAdapter(Context context, List<ListItem> listItems) {
        super();
        this.context = context;
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
        final ListItem listItem = itemList.get(position);
        ImageView imageView = ((ItemListViewHolder) holder).poster;
        String imageURL = listItem.getImageURL();
        if(!imageURL.equals("null")) {
            Picasso.get().load("https://image.tmdb.org/t/p/w300" + listItem.getImageURL()).into(imageView);
        }
        ((ItemListViewHolder)holder).title.setText(listItem.getTitle());
        ((SearchListAdapter.ItemListViewHolder) holder).rating.setText(listItem.getRating() + "/10");
        if(listItem.getType().equals("m")) {
            ((SearchListAdapter.ItemListViewHolder)holder).type.setText("Movie");
        } else {
            ((SearchListAdapter.ItemListViewHolder)holder).type.setText("TV Show");
        }
        ((SearchListAdapter.ItemListViewHolder)holder).description.setText(listItem.getDescription());
        ((ItemListViewHolder)holder).title.setVisibility(View.VISIBLE);
        ((SearchListAdapter.ItemListViewHolder)holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("uid", listItem.getUid());
                intent.putExtra("title", listItem.getTitle());
                intent.putExtra("description", listItem.getDescription());
                intent.putExtra("rating", listItem.getRating());
                intent.putExtra("imageURL", listItem.getImageURL());
                intent.putExtra("type", listItem.getType());
                context.startActivity(intent);
            }
        });
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
        private TextView title, rating, type, description;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            poster = itemView.findViewById(R.id.movie_poster);
            title = itemView.findViewById(R.id.movie_title);
            rating = itemView.findViewById(R.id.item_rating);
            type = itemView.findViewById(R.id.item_type);
            description = itemView.findViewById(R.id.item_description);
        }
    }
}
