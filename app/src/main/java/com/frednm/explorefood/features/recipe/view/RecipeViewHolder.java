package com.frednm.explorefood.features.recipe.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.databinding.ItemRecipeBinding;
import com.frednm.explorefood.features.recipe.RecipeViewModel;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private ItemRecipeBinding binding;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemRecipeBinding.bind(itemView);
    }

    protected void bindTo(Recipe recipe, RecipeViewModel viewModel) {
        binding.setRecipe(recipe);
        binding.setViewmodel(viewModel);
    }
}
