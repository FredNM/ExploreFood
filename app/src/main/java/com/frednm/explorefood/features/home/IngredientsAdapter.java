package com.frednm.explorefood.features.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frednm.explorefood.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder>{

    // CALLBACK
    public interface Listener { void onClickDeleteButton(int position); }
    private final Listener callback;

    // FOR DATA
    private List<String> ingredients = new ArrayList<>();

    // CONSTRUCTOR
    public IngredientsAdapter(Listener callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.updateWithIngredient(this.ingredients.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void updateData(List<String> ingredients){
        this.ingredients = ingredients;
        this.notifyDataSetChanged();
    }
}
