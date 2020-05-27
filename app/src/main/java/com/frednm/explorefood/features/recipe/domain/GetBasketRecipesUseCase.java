package com.frednm.explorefood.features.recipe.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.repository.RecipeRepository;
import com.frednm.explorefood.data.repository.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class GetBasketRecipesUseCase {

    public RecipeRepository repository;

    @Inject
    public GetBasketRecipesUseCase(RecipeRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<Recipe>>> invoke(){
        return Transformations.map(repository.getBasketRecipes(),this::transform);
    }

    // Useless so far, but could be interesting to apply something here
    private Resource<List<Recipe>> transform(Resource<List<Recipe>> data) {
        return data;
    }
}
