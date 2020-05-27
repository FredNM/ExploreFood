package com.frednm.explorefood.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ZomatoApiResult {

    @SerializedName("results_shown")
    @Expose
    private Integer resultsShown;

    @SerializedName("restaurants")
    @Expose
    private List<Restaurants> restaurants;

    public ZomatoApiResult(Integer resultsShown, List<Restaurants> restaurants) {
        this.resultsShown = resultsShown;
        this.restaurants = restaurants;
    }

    public Integer getResultsShown() {
        return resultsShown;
    }

    public void setResultsShown(Integer resultsShown) {
        this.resultsShown = resultsShown;
    }

    public List<Restaurants> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurants> restaurants) {
        this.restaurants = restaurants;
    }
}
