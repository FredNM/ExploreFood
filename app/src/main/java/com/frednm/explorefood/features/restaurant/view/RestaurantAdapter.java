package com.frednm.explorefood.features.restaurant.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.frednm.explorefood.R;
import com.frednm.explorefood.data.model.Restaurant;
import com.frednm.explorefood.features.restaurant.RestaurantViewModel;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantAdapter() {    }


    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        holder.bindTo(restaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void updateData(List<Restaurant> items) {
        RestaurantItemDiffCallback diffCallback = new RestaurantItemDiffCallback(restaurants, items);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        restaurants.clear();
        restaurants.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }
}
