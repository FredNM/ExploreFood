package com.frednm.explorefood.features.recipe.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.frednm.explorefood.R;
import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.repository.utils.Resource;

import java.util.List;

public class RecipeBinding {

    static RequestOptions requestOptions = new RequestOptions()
            .error(R.drawable.ic_error_image_24dp)
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).apply(requestOptions).into(view);
        //Log.d("DataBinding", "image Url : "+imageUrl);
    }

    @BindingAdapter("showWhenLoading")
    public static <T> void showWhenLoadingData(SwipeRefreshLayout view, Resource<T> resource) {
        Log.d("RecipeBinding", "Resource: "+resource);
        if (resource != null) {
            view.setRefreshing(resource.getStatus() == Resource.Status.LOADING);
        }
    }

    @BindingAdapter("items")
    public static void setItems (RecyclerView recyclerView, @NonNull Resource<List<Recipe>> resource) {
        if (resource != null) {
            if(!(resource.getData() == null)) {
                if (recyclerView.getAdapter() instanceof RecipeAdapter) {
                    ((RecipeAdapter) recyclerView.getAdapter()).updateData(resource.getData());
                }
            }
        }
    }

    @BindingAdapter("showWhenEmptyList")
    public static void showMessageErrorWhenEmptyList(View view, Resource<List<Recipe>> resource) {
        if (resource != null) {
            Boolean bool = resource.getStatus() == Resource.Status.ERROR
                    && resource.getData() != null
                    && resource.getData().isEmpty() ;
            view.setVisibility(bool ? View.VISIBLE : View.GONE);
        }
    }

    @BindingAdapter("isSelected")
    public static void setSelected(View view, Boolean isUserFavorite) { // set View State
        if (isUserFavorite != null) {
            Log.d("DataBinding", "isUserFavorite: "+isUserFavorite);
            view.setSelected(isUserFavorite);
            /** if (isUserFavorite) {
                view.setColorFilter(getRe);
            }
            else {} */
        }
    }

    @BindingAdapter("activateSearchButton")
    public static void setEnabled(View view, Resource<List<Recipe>> resource) { // set View State
        if (resource != null) {
            Boolean bool = resource.getData() != null ;
            view.setEnabled(bool);
        }
    }


}
