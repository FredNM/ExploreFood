package com.frednm.explorefood.features.restaurant.view;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.frednm.explorefood.data.model.Restaurant;
import com.frednm.explorefood.data.repository.utils.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RestaurantBinding {

    @BindingAdapter("cardColor")
    public static void setColor(CardView view, String rating)
    {
        float rate = Float.parseFloat(rating);
        if (rate>=4) {
            view.setCardBackgroundColor(Color.parseColor("#76ff03"));
        }
        else if(rate>=2 && rate<4)
        {
            view.setCardBackgroundColor(Color.parseColor("#ffd740"));
        }
        else
            view.setCardBackgroundColor(Color.parseColor("#ff6e40"));
    }

    @BindingAdapter("showWhenEmptyRestaurantList")
    public static void showMessageErrorWhenEmptyRestaurantList(View view, Resource<List<Restaurant>> resource) {
        if (resource != null) {
            Boolean bool = resource.getStatus() == Resource.Status.ERROR
                    && resource.getData() != null
                    && resource.getData().isEmpty() ;
            view.setVisibility(bool ? View.VISIBLE : View.GONE);
        }
    }

    @BindingAdapter("itemsRestaurant")
    public static void setItems (RecyclerView recyclerView, @NonNull Resource<List<Restaurant>> resource) {
        if (resource != null) {
            if(!(resource.getData() == null)) {
                if (recyclerView.getAdapter() instanceof RestaurantAdapter) {
                    ((RestaurantAdapter) recyclerView.getAdapter()).updateData(resource.getData());
                }
            }
        }
    }
}
