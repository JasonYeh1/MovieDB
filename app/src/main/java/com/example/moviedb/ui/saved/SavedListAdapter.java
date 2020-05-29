package com.example.moviedb.ui.saved;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviedb.R;
import com.example.moviedb.db.AppDatabase;
import com.example.moviedb.model.SavedItem;
import com.example.moviedb.ui.details.DetailActivity;
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
        final SavedItem listItem = itemList.get(position);
        ImageView imageView = ((ItemListViewHolder) holder).poster;
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        String imageURL = listItem.getImageURL();
        if(!imageURL.equals("null")) {
            Picasso.get().load("https://image.tmdb.org/t/p/w300" + listItem.getImageURL()).placeholder(circularProgressDrawable).noFade().into(imageView);
        }
        Log.d("Sizes", "" + imageView.getDrawable().getIntrinsicHeight() + " " + imageView.getDrawable().getIntrinsicHeight());
        ((ItemListViewHolder)holder).title.setText(listItem.getTitle());
        ((SavedListAdapter.ItemListViewHolder) holder).rating.setText(listItem.getRating() + "/10");
        if(listItem.getType().equals("m")) {
            ((SavedListAdapter.ItemListViewHolder)holder).type.setText("Movie");
        } else {
            ((SavedListAdapter.ItemListViewHolder)holder).type.setText("TV Show");
        }
        ((SavedListAdapter.ItemListViewHolder)holder).description.setText(listItem.getDescription());
        ((ItemListViewHolder)holder).title.setVisibility(View.VISIBLE);
        ((SavedListAdapter.ItemListViewHolder)holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("saveButton", false);
                intent.putExtra("uid", listItem.getUid());
                intent.putExtra("title", listItem.getTitle());
                intent.putExtra("description", listItem.getDescription());
                intent.putExtra("rating", listItem.getRating());
                intent.putExtra("imageURL", listItem.getImageURL());
                intent.putExtra("type", listItem.getType());
                context.startActivity(intent);
            }
        });

        ((ItemListViewHolder) holder).view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete item?")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppDatabase.getInstance(context).savedItemDao().deleteSavedItem(listItem.getUid());
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
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
