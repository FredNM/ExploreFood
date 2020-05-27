package com.frednm.explorefood.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.frednm.explorefood.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

// See https://stackoverflow.com/questions/45677230/android-room-persistence-library-upsert

@Dao
abstract class BaseDao<T> {
    /**
     * Insert an object in the database.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(T obj);

    /**
     * Insert an array of objects in the database.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract List<Long> insert(List<T> obj); // List long has id of the insertion result, which will be -1 for not insert items !

    /**
     * Update an object from the database.
     */
    @Update
    public abstract void update(T obj);

    /**
     * Update an array of objects from the database.
     */
    @Update
    public abstract void update(List<T> obj);

    /**
     * For this specific update, just set the isOnlineRecipe colums of the recipes to true
     */
    @Query("UPDATE Recipe SET isOnlineRecipe = 1 WHERE label = :recipeLabel AND imageUrl = :recipeImageUrl ")
    // j'utilise ces 2 colonnes dans le WHERE car c'est les 2 ma primary key !
    public abstract void updateQ(String recipeLabel, String recipeImageUrl);

    /**
     * Delete an object from the database
     */
    @Delete
    public abstract void delete(T obj);

    @Transaction
    public void upsert(T obj) {
        long id = insert(obj);
        if (id == -1) {
            update(obj);
        }
    }

    @Transaction
    public void upsert(List<T> objList) { // Insert a list of items; the items already existing in the DB (insertion failed), update them !
        List<Long> insertResult = insert(objList);
        List<T> updateList = new ArrayList<>();

        for (int i = 0; i < insertResult.size(); i++) {
            if (insertResult.get(i) == -1) {
                updateList.add(objList.get(i));
            }
        }

        if (!updateList.isEmpty()) {
            update(updateList);
        }
    }

    @Transaction
    public void upsertQ(List<T> objList) { // upsert where the update was performed with @Query and not @Update
        List<Long> insertResult = insert(objList);
        List<T> updateList = new ArrayList<>();

        for (int i = 0; i < insertResult.size(); i++) {
            if (insertResult.get(i) == -1) {
                updateList.add(objList.get(i));
            }
        }

        if (!updateList.isEmpty()) { // Comme dans @Query on ne peut pas traiter de liste (contrairement à @Update), on va "casser" la liste en
            // ses différents items individuels et faire le traitement sur chaque item individuel !!!
            for (int i = 0; i < updateList.size(); i++) {
                Recipe recipe = (Recipe) updateList.get(i); // Je fais ça parce que je sais que l'item sera forcément un Recipe
                updateQ(recipe.getLabel(),recipe.getImageUrl());
                // T obj = updateList.get(i);
                // if (obj instanceof Recipe) {
                //     updateQ(((Recipe) obj).getLabel(), ((Recipe) obj).getImageUrl());
                // }
            }
        }
    }

}

