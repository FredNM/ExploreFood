package com.frednm.explorefood.features.home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frednm.explorefood.databinding.ItemIngredientBinding;

import java.lang.ref.WeakReference;

public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ItemIngredientBinding binding;
    private WeakReference<IngredientsAdapter.Listener> callbackWeakRef;

    public IngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemIngredientBinding.bind(itemView);
    }

    public void updateWithIngredient(String ingredient, IngredientsAdapter.Listener callback){
        this.callbackWeakRef = new WeakReference<>(callback);
        binding.itemText.setText(ingredient);
        binding.itemRemove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        IngredientsAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}
