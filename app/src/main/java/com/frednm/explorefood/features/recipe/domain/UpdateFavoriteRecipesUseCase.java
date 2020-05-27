package com.frednm.explorefood.features.recipe.domain;

import com.frednm.explorefood.data.repository.RecipeRepository;

import javax.inject.Inject;

public class UpdateFavoriteRecipesUseCase {

    public RecipeRepository repository;

    @Inject
    public UpdateFavoriteRecipesUseCase(RecipeRepository repository) {
        this.repository = repository;
    }

    public void invoke(Boolean isSelected, String label, String imageUrl){
        repository.updateFavoriteRecipes(isSelected, label, imageUrl);
    }
}
