package com.frednm.explorefood.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("aggregate_rating")
    @Expose
    private String aggregate;
    @SerializedName("rating_text")
    @Expose
    private String text;
    @SerializedName("votes")
    @Expose
    private String votes;

    public Rating(String aggregate, String text, String votes) {
        this.aggregate = aggregate;
        this.text = text;
        this.votes = votes;
    }

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
