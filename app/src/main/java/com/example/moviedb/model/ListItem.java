package com.example.moviedb.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
/**
 * Room model representing a ListItem
 * This table is only used for providing data
 * for views and not for persisting it
 */
public class ListItem {
    @PrimaryKey
    @NonNull
    //Attribute representing the uid of the item
    @ColumnInfo(name = "uid", typeAffinity = ColumnInfo.TEXT)
    private String uid;

    //Attribute representing the title of the item
    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    private String title;

    //Attribute representing the description of the item
    @ColumnInfo(name = "description", typeAffinity = ColumnInfo.TEXT)
    private String description;

    //Attribute representing the rating of the item
    @ColumnInfo(name = "rating", typeAffinity = ColumnInfo.TEXT)
    private String rating;

    //Attribute representing the imageURL of the item
    @ColumnInfo(name = "imageURL", typeAffinity = ColumnInfo.TEXT)
    private String imageURL;

    //Attribute representing the type of the item
    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.TEXT)
    private String type;

    public ListItem(String uid, String title, String description, String rating, String imageURL, String type) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.imageURL = imageURL;
        this.type = type;
    }

    public void setUid(String uid) {
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

    public String getUid(){
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
