package com.frednm.explorefood.features.recipe.view;

import androidx.recyclerview.widget.DiffUtil;

import com.frednm.explorefood.data.model.Recipe;

import java.util.List;

public class RecipeItemDiffCallback extends DiffUtil.Callback {

    private List<Recipe> oldList;
    private List<Recipe> newList;

    public RecipeItemDiffCallback(List<Recipe> oldList, List<Recipe> newList) {
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
        return oldList.get(oldItemPosition).getLabel() == newList.get(newItemPosition).getLabel()
                && oldList.get(oldItemPosition).getImageUrl() == newList.get(newItemPosition).getImageUrl();
    }
}
