package com.example.moviedb.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListItem {
    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    private String title;

    @ColumnInfo(name = "description", typeAffinity = ColumnInfo.TEXT)
    private String description;

    @ColumnInfo(name = "rating", typeAffinity = ColumnInfo.TEXT)
    private String rating;

    @ColumnInfo(name = "imageURL", typeAffinity = ColumnInfo.TEXT)
    private String imageURL;

    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.TEXT)
    private String type;

    public ListItem(String title, String description, String rating, String imageURL, String type) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.imageURL = imageURL;
        this.type = type;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setType(String types) {
        this.type = type;
    }

    public int getUid(){
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getType() {
        return type;
    }

}
