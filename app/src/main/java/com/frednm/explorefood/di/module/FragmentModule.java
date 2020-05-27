package com.frednm.explorefood.di.module;



import com.frednm.explorefood.common.base.BaseFragment;
import com.frednm.explorefood.features.home.HomeFragment;
import com.frednm.explorefood.features.recipe.DetailRecipeFragment;
import com.frednm.explorefood.features.recipe.RecipeFragment;
import com.frednm.explorefood.features.restaurant.RestaurantFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract BaseFragment contributeBaseFragment();

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract RecipeFragment contributeRecipeFragment();

    @ContributesAndroidInjector
    abstract DetailRecipeFragment contributeDetailRecipeFragment();

    @ContributesAndroidInjector
    abstract RestaurantFragment contributeRestaurantFragment();
}
