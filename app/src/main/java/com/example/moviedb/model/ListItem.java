package com.example.moviedb.model;

public class ListItem {
    private String title;
    private String description;
    private String rating;
    private String imageURL;
    private String type;

    public ListItem(String title, String description, String rating, String imageURL, String type) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.imageURL = imageURL;
        this.type = type;
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
}
