package com.frednm.explorefood.features.recipe.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.frednm.explorefood.R;
import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.features.recipe.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private RecipeViewModel viewModel;

    public RecipeAdapter(RecipeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bindTo(recipes.get(position), viewModel);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateData(List<Recipe> items) {
        RecipeItemDiffCallback diffCallback = new RecipeItemDiffCallback(recipes, items);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        recipes.clear();
        recipes.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }
}
