package com.frednm.explorefood.data.local;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.frednm.explorefood.data.model.Recipe;

import java.util.List;

@Dao
public abstract class RecipeDao extends BaseDao<Recipe> {
    // TODO All the work

    // -- SELECT
    // In Room, true = 1, false = 0. Read here https://stackoverflow.com/questions/47730820/hardcode-boolean-query-in-room-database
    @Query("SELECT * FROM Recipe WHERE isOnlineRecipe = 1") // WHERE isOnlineRecipe = 1 ")
    public abstract List<Recipe> getOnlineRecipes(); // get all the new online results

    @Query("SELECT * FROM Recipe WHERE isUserFavorite = 1 ")
    public abstract List<Recipe> getFavoriteRecipes();

    @Query("SELECT * FROM Recipe WHERE isInBasket = 1 ")
    public abstract List<Recipe> getBasketRecipes();

    // --- INSERT ONLINE RESULT
    public void insertOnLineResults(List<Recipe> recipes){
        //insert(recipes);
        upsertQ(recipes);
    }

    // --- UPDATE
    @Query("UPDATE Recipe SET isUserFavorite = :favorite WHERE label = :label AND imageUrl = :imageUrl ")
    public abstract int updateFavoriteRecipe(Boolean favorite, String label, String imageUrl);

    @Query("UPDATE Recipe SET isInBasket = :inBasket WHERE label = :label AND imageUrl = :imageUrl ")
    public abstract int updateBasketRecipe(Boolean inBasket, String label, String imageUrl);


    // -- DELETE
    @Query("DELETE FROM Recipe WHERE isOnlineRecipe = 1 ")
    abstract void deleteOnlineRecipes();

    @Query("DELETE FROM Recipe WHERE isInBasket = 1 ")
    abstract void deleteBasketRecipes();

    // --- UPDATE BEFORE DELETING
    @Query("UPDATE Recipe SET isInBasket = 0 WHERE isUserFavorite = 1 ")
    public abstract int updateFavoriteBasketRecipes();

    @Query("UPDATE Recipe SET isOnlineRecipe = 0 WHERE isUserFavorite = 1 ")
    public abstract int updateFavoriteOnlineRecipes();

    // --- CLEANING BASKET OR ONLINE RESULTS : before deleting, first save recipes which are user favorites in the DB
    @Transaction
    public void cleanBasket() {
        updateFavoriteBasketRecipes(); // enleve du panier (i.e. met isInBasket à 0) les recipes qui sont userfavorites
        deleteBasketRecipes(); // supprime donc maintenant les recipes où isInBasket = 1
    }

    @Transaction
    public void cleanOnlineResults() {
        updateFavoriteOnlineRecipes();
        deleteOnlineRecipes();
    }

    //TODO For next project : configure Flowable to update list of favorite movies. Don't forget to avoid false positive notif !

}
