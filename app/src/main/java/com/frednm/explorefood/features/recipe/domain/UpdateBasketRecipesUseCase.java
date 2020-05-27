package com.frednm.explorefood.features.recipe.domain;

import com.frednm.explorefood.data.repository.RecipeRepository;

import javax.inject.Inject;

public class UpdateBasketRecipesUseCase {

    public RecipeRepository repository;

    @Inject
    public UpdateBasketRecipesUseCase(RecipeRepository repository) {
        this.repository = repository;
    }

    public void invoke(Boolean isSelected, String label, String imageUrl){
        repository.updateBasketRecipes(isSelected, label, imageUrl);
    }
}
