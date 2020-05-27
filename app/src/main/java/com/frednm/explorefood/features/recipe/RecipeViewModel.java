package com.frednm.explorefood.features.recipe;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.frednm.explorefood.R;
import com.frednm.explorefood.common.base.BaseViewModel;
import com.frednm.explorefood.common.utils.Event;
import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.repository.utils.Resource;
import com.frednm.explorefood.features.recipe.domain.GetBasketRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.GetFavoriteRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.GetOnlineRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.UpdateBasketRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.UpdateFavoriteRecipesUseCase;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

public class RecipeViewModel extends BaseViewModel {

    public interface Listener {
        void executeBindingMethods();
        void navigateToDetail();
        void navigateToRestaurant();
    }
    private WeakReference<Listener> callbackListener;

    public interface DetailListener {
        void openWebPage(String url);
    }
    private WeakReference<DetailListener> callbackDetailListener;

    // --- Use cases
    private GetOnlineRecipesUseCase getOnlineRecipesUseCase;
    private GetFavoriteRecipesUseCase getFavoriteRecipesUseCase;
    private GetBasketRecipesUseCase getBasketRecipesUseCase;
    private UpdateFavoriteRecipesUseCase updateFavoriteRecipesUseCase;
    private UpdateBasketRecipesUseCase updateBasketRecipesUseCase;

    // --- For data shown on RecipeFragment
    private MediatorLiveData<Resource<List<Recipe>>> _recipes = new MediatorLiveData<>();
    public LiveData<Resource<List<Recipe>>> recipes = _recipes;
    private LiveData<Resource<List<Recipe>>> recipesSource = new MutableLiveData<>();

    // --- For data shown on RecipeDetailFragment.
    private MediatorLiveData<Recipe> _recipe = new MediatorLiveData<>();
    public LiveData<Recipe> recipe = _recipe;
    private Recipe recipeData;
    // private LiveData<Resource<Recipe>> recipeSource = new MutableLiveData<>();

    private String action;

    List<String> ingredients;

    private Boolean viewState;

    // --- CONSTRUCTOR
    @Inject
    public RecipeViewModel(GetOnlineRecipesUseCase getOnlineRecipesUseCase, GetFavoriteRecipesUseCase getFavoriteRecipesUseCase, GetBasketRecipesUseCase getBasketRecipesUseCase, UpdateFavoriteRecipesUseCase updateFavoriteRecipesUseCase, UpdateBasketRecipesUseCase updateBasketRecipesUseCase) {
        this.getOnlineRecipesUseCase = getOnlineRecipesUseCase;
        this.getFavoriteRecipesUseCase = getFavoriteRecipesUseCase;
        this.getBasketRecipesUseCase = getBasketRecipesUseCase;
        this.updateFavoriteRecipesUseCase = updateFavoriteRecipesUseCase;
        this.updateBasketRecipesUseCase = updateBasketRecipesUseCase;
    }

    //--- GETTERS OF RECIPEDATA
    public Recipe getRecipeData() {
        return recipeData;
    }

    //  --- USER ACTIONS  : For RecipeFragment
    public void loadEdamamResearchData(List<String> ingredients, String action){
        //TODO
        this.action = action;
        this.ingredients = ingredients;
        getOnlineRecipes(ingredients);
    }

    public void loadFavoriteData(String action){
        //TODO
        this.action = action;
        getFavoriteRecipes();
    }

    public void loadBasketData(String action){
        //TODO
        this.action = action;
        getBasketRecipes();
    }

    public void userRefreshesItems(){
        // getOnlineRecipes(ingredients);
    } // ingredients ci, c'est la liste d'ingredient qu'on a récupéré lors du chargement de données !

    public void userClicksOnItem(Recipe recipe) {
        this.recipeData = recipe;
        this.callbackListener.get().navigateToDetail();
    }

    public void userClicksOnFavoriteButton(View view, Recipe recipe) {
        if (view.isSelected()) { //isSelected.get(movieId)
            Log.d("RecipeViewModel","Button becomes not selected");
            viewState = false ;
        } else {
            Log.d("DetailViewModel","Button becomes selected");
            viewState = true ;
        }
        //view.setSelected(favoriteState); // on change temporairement l'état de la vue à la suite du clic.
        updateFavoriteRecipesUseCase.invoke(viewState, recipe.getLabel(), recipe.getImageUrl());
        if (action.equals("favorite")) {
            getFavoriteRecipes();
        }
        view.setSelected(viewState);
    }

    public void userClicksOnBasketButton(View view, Recipe recipe){
        if (view.isSelected()) { //isSelected.get(movieId)
            Log.d("RecipeViewModel","Button becomes not selected");
            viewState = false ;
        } else {
            Log.d("DetailViewModel","Button becomes selected");
            viewState = true ;
        }
        //view.setSelected(favoriteState); // on change temporairement l'état de la vue à la suite du clic.
        updateBasketRecipesUseCase.invoke(viewState, recipe.getLabel(), recipe.getImageUrl());
        if (action.equals("basket")) {
            getBasketRecipes();
        }
        view.setSelected(viewState);
    }

    public void userClicksOnSearchRestaurantButton(View view) {
        this.callbackListener.get().navigateToRestaurant();
    }

    // --- USER ACTIONS : For DetailRecipeFragment
    public void userClicksOnButtonWebPage(){
        //TODO Open the web page !
        this.callbackDetailListener.get().openWebPage(recipeData.getRecipeUrl());
    }

    // --- LOCAL METHODS FOR USER ACTIONS
    private void getOnlineRecipes(List<String> ingredients) {
        _recipes.removeSource(recipesSource);
        recipesSource = getOnlineRecipesUseCase.invoke(true, ingredients);         // force refresh toujours à true !!!
        //recipesSource = recipeRepository.getOnlineXxx(true,ingredients);
        _recipes.addSource(recipesSource, this::treatData);
    }

    private void getFavoriteRecipes() {
        _recipes.removeSource(recipesSource);
        recipesSource = getFavoriteRecipesUseCase.invoke();
        _recipes.addSource(recipesSource, this::treatData);
    }

    private void getBasketRecipes() {
        _recipes.removeSource(recipesSource);
        recipesSource = getBasketRecipesUseCase.invoke();
        _recipes.addSource(recipesSource, this::treatData);
    }

    private void treatData(Resource<List<Recipe>> resource) {
        this._recipes.setValue(resource);
        if(resource.getData() != null) {
            Log.d("RecipeViewModel", "Number of data : " + resource.getData().size());
        } else {
            Log.d("RecipeViewModel", "Null data : ");
        }
        if (resource.getStatus() == Resource.Status.ERROR) _snackbarError.setValue(new Event<>(R.string.an_error_happened));
        if (resource.getStatus() == Resource.Status.NONETWORK) _snackbarError.setValue(new Event<>(R.string.no_network));
    }

    // --- LISTENERS
    public void implementedFragmentListener(RecipeViewModel.Listener callbackListener) {
        this.callbackListener  = new WeakReference<>(callbackListener);
    }

    public void implementedDetailFragmentListener(RecipeViewModel.DetailListener callbackDetailListener) {
        this.callbackDetailListener  = new WeakReference<>(callbackDetailListener);
    }

}
