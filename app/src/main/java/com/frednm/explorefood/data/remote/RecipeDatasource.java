package com.frednm.explorefood.data.remote;

import android.util.Log;

import com.frednm.explorefood.data.model.EdamamApiResult;
import com.frednm.explorefood.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class RecipeDatasource {

    private static final String appId = "747b0dee"; // TODO Put your API key here !
    private static final String appKey = "69140fe35b0a960415104bc494d35d8a"; // TODO Put your API key here !
    private RecipeService recipeService ;

    @Inject
    public RecipeDatasource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public Call<EdamamApiResult> fetchRecipes(List<String> ingredients){
        Log.d("Recipe DataSource", "Fetching recipes");
        return recipeService.fetchRecipes(ingredients,appId,appKey);
    }
}
