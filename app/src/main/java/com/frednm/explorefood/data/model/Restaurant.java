package com.frednm.explorefood.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Restaurant {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String restaurantId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("cuisines")
    @Expose
    private String cuisines;
    @SerializedName("thumb")
    @Expose
    private String thumbnail;
    @SerializedName("menu_url")
    @Expose
    private String menu;
    @SerializedName("user_rating")
    @Expose
    private Rating rating;
    @SerializedName("location")
    @Expose
    private Location location;

    public Restaurant(String name, String restaurantId, String url, String cuisines, String thumbnail, String menu, Rating rating, Location location) {
        this.name = name;
        this.restaurantId = restaurantId;
        this.url = url;
        this.cuisines = cuisines;
        this.thumbnail = thumbnail;
        this.menu = menu;
        this.rating = rating;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
