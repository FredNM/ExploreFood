package com.frednm.explorefood.features.restaurant.view;

import androidx.recyclerview.widget.DiffUtil;

import com.frednm.explorefood.data.model.Restaurant;

import java.util.List;

public class RestaurantItemDiffCallback extends DiffUtil.Callback {

    private List<Restaurant> oldList;
    private List<Restaurant> newList;

    public RestaurantItemDiffCallback(List<Restaurant> oldList, List<Restaurant> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false ; //oldList.get(oldItemPosition).getLabel() == newList.get(newItemPosition).getLabel()
                // && oldList.get(oldItemPosition).getImageUrl() == newList.get(newItemPosition).getImageUrl();
    }
}
