package com.frednm.explorefood.features.recipe.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.repository.RecipeRepository;
import com.frednm.explorefood.data.repository.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class GetOnlineRecipesUseCase {

    public RecipeRepository repository;

    @Inject
    public GetOnlineRecipesUseCase(RecipeRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<Recipe>>> invoke(Boolean forceRefresh, List<String> ingredients){
        Log.d("GetOnlineRecipesUseCase", "entering in invoke" );
        return Transformations.map(repository.getOnlineRecipes(forceRefresh, ingredients),this::transform);
    }

    // Useless so far, but could be interesting to apply something here
    private Resource<List<Recipe>> transform(Resource<List<Recipe>> data) {
        Log.d("GetOnlineRecipesUseCase", "entering in tranform ") ;
        return data;
    }

}
