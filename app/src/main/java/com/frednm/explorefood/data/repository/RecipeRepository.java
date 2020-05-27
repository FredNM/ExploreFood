package com.frednm.explorefood.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.frednm.explorefood.data.local.RecipeDao;
import com.frednm.explorefood.data.model.EdamamApiResult;
import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.remote.RecipeDatasource;
import com.frednm.explorefood.data.repository.utils.ConnectivityLiveData;
import com.frednm.explorefood.data.repository.utils.NetworkBoundResource;
import com.frednm.explorefood.data.repository.utils.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class RecipeRepository {

    private RecipeDatasource recipeDatasource;
    private RecipeDao recipeDao;
    private final ConnectivityLiveData connectivityLiveData;

    Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public RecipeRepository(RecipeDatasource recipeDatasource, RecipeDao recipeDao, ConnectivityLiveData connectivityLiveData) {
        this.recipeDatasource = recipeDatasource;
        this.recipeDao = recipeDao;
        this.connectivityLiveData = connectivityLiveData;
    }

    public LiveData<Resource<List<Recipe>>> getOnlineRecipes(Boolean forceRefresh, List<String> ingredients){
        return new NetworkBoundResource<List<Recipe>,EdamamApiResult>(connectivityLiveData){
            @Override
            protected List<Recipe> processResponse(EdamamApiResult response) {
                List<Recipe> recipes = new ArrayList<>();
                if (response.getHits() != null) {
                    for (int i = 0; i < response.getHits().size(); i++) {
                        recipes.add(response.getHits().get(i).getRecipe());
                        recipes.get(i).setOnlineRecipe(true); // Bien dire que ça vient d'en ligne
                    }
                }
                Log.d("RecipeRepository", "Number of items " + response.getHits().size());
                return recipes;
            }

            @Override
            protected void saveCallResults(@NonNull List<Recipe> items) {
                recipeDao.cleanOnlineResults(); // Avant de sauvegarder les nouveaux résultats de la requête, enlève les anciens
                Log.d("RecipeRepository", "Number of items saved " + items.size());
                for (int i=0; i < items.size(); i++) {
                    Log.d("RecipeRepository", "Label : " + items.get(i).getLabel());
                }
                recipeDao.insertOnLineResults(items);
            }

            @Override
            protected Boolean shouldFetch(@Nullable List<Recipe> data) {
                return data == null || forceRefresh  ;
            }

            @NonNull
            @Override
            protected List<Recipe> loadFromDb() {
                Log.d("RecipeRepository", "Number of items from Db " + recipeDao.getOnlineRecipes().size());
                return recipeDao.getOnlineRecipes();
            }

            @NonNull
            @Override
            protected Call<EdamamApiResult> createCallAsync() {
                return recipeDatasource.fetchRecipes(ingredients);
            }
        }.asLiveData();
    }

    // Those two methods do not need network, and always take data in DB ! But I use NetworkBoundResource simply because it allows me to
    // to do the DB request in a background thread !
    // For example, this will not work
    //        executor.execute(() -> {
    //    return recipeDao.getFavoriteRecipes(); });
    public LiveData<Resource<List<Recipe>>> getFavoriteRecipes(){
         return new NetworkBoundResource<List<Recipe>,EdamamApiResult>(connectivityLiveData){
            @Override
            protected List<Recipe> processResponse(EdamamApiResult response) {
                return null; // Nothing to do
            }
            @Override
            protected void saveCallResults(@NonNull List<Recipe> item) {
                // Nothing to do
            }
            @Override
            protected Boolean shouldFetch(@Nullable List<Recipe> data) {
                return false  ; // no Data to take online
            }
            @NonNull
            @Override
            protected List<Recipe> loadFromDb() {
                return recipeDao.getFavoriteRecipes();
            }
            @NonNull
            @Override
            protected Call<EdamamApiResult> createCallAsync() {
                List<String> fictive = new ArrayList<>();
                return recipeDatasource.fetchRecipes(fictive); // it will never work, since shouldFetch is always false.
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Recipe>>> getBasketRecipes(){
         return new NetworkBoundResource<List<Recipe>,EdamamApiResult>(connectivityLiveData){
            @Override
            protected List<Recipe> processResponse(EdamamApiResult response) {
                return null; // Nothing to do
            }
            @Override
            protected void saveCallResults(@NonNull List<Recipe> item) {
                // Nothing to do
            }
            @Override
            protected Boolean shouldFetch(@Nullable List<Recipe> data) {
                return false  ; // no Data to take online
            }
            @NonNull
            @Override
            protected List<Recipe> loadFromDb() {
                return recipeDao.getBasketRecipes();
            }
            @NonNull
            @Override
            protected Call<EdamamApiResult> createCallAsync() {
                List<String> fictive = new ArrayList<>();
                return recipeDatasource.fetchRecipes(fictive); // it will never work, since shouldFetch is always false.
            }
        }.asLiveData();
    }

    // Oblige to do this task in background thread, otherwise Room will complain.
    // See https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
    public void updateFavoriteRecipes(Boolean isSelected, String label, String imageUrl){
        executor.execute(() -> {
            recipeDao.updateFavoriteRecipe(isSelected, label, imageUrl);
        });
    }

    public void updateBasketRecipes(Boolean isSelected, String label, String imageUrl){
        executor.execute(() -> {
            recipeDao.updateBasketRecipe(isSelected, label, imageUrl);
        });
    }

}
