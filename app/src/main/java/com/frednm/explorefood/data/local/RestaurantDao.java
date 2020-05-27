package com.frednm.explorefood.data.local;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.model.Restaurant;

import java.util.List;

@Dao
public abstract class RestaurantDao extends BaseDao<Restaurant> {
    // TODO All the work

    // -- SELECT
    // In Room, true = 1, false = 0. Read here https://stackoverflow.com/questions/47730820/hardcode-boolean-query-in-room-database
    @Query("SELECT * FROM Restaurant") // WHERE isOnlineRecipe = 1 ")
    public abstract List<Restaurant> getRestaurants(); // get all the new online results

    // -- DELETE
    @Query("DELETE FROM Restaurant ") // Supprime tout ce qui est dans la BDD
    abstract void deleteRestaurants();

    @Transaction
    public void saveRestaurants(List<Restaurant> restaurants) {
        deleteRestaurants(); // supprime donc maintenant les recipes où isInBasket = 1
        insert(restaurants);
    }


}
