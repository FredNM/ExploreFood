package com.frednm.explorefood.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EdamamApiResult { //<T> {

    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

  /**  @SerializedName("hits")
    private List<T> hits ;

    public List<T> getHits() {
        return hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    } */
}
