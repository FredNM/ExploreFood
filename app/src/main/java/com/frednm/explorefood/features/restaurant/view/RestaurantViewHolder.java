package com.frednm.explorefood.features.restaurant.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frednm.explorefood.data.model.Restaurant;
import com.frednm.explorefood.databinding.ItemRestaurantBinding;
import com.frednm.explorefood.features.restaurant.RestaurantViewModel;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private ItemRestaurantBinding binding;

    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemRestaurantBinding.bind(itemView);
    }

    protected void bindTo(Restaurant restaurant) {
        binding.setRestaurant(restaurant);
    }
}
