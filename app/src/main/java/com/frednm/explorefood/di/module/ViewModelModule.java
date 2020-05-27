package com.frednm.explorefood.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.frednm.explorefood.common.factory.FactoryViewModel;
import com.frednm.explorefood.di.key.ViewModelKey;
import com.frednm.explorefood.features.recipe.RecipeViewModel;
import com.frednm.explorefood.features.restaurant.RestaurantViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantViewModel.class)
    abstract ViewModel bindRestaurantViewModel(RestaurantViewModel restaurantViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindRecipeViewModel(RecipeViewModel recipeViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
