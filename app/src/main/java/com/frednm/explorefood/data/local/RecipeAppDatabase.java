package com.frednm.explorefood.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.model.Restaurant;

@Database(entities = {Recipe.class, Restaurant.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RecipeAppDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile RecipeAppDatabase INSTANCE;

    // --- DAO ---
    public abstract RecipeDao recipeDao();
    public abstract RestaurantDao restaurantDao();

}
