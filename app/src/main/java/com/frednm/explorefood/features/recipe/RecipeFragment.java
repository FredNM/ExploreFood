package com.frednm.explorefood.features.recipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frednm.explorefood.R;
import com.frednm.explorefood.common.base.BaseFragment;
import com.frednm.explorefood.common.base.BaseViewModel;
import com.frednm.explorefood.databinding.FragmentRecipeBinding;
import com.frednm.explorefood.features.recipe.view.RecipeAdapter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.frednm.explorefood.MainActivity.FOR_BOTTOM_BAR;

public class RecipeFragment extends BaseFragment implements RecipeViewModel.Listener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @NonNull
    private RecipeViewModel viewModel;

    private FragmentRecipeBinding binding;

    private RecipeFragmentArgs args;
    private List<String> ingredients;

    // For SharedPreferences, having the bottom selected on the Bottom App Bar
    static SharedPreferences sharedPref;
    String action;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner()); // For LiveData in View ! Les observer devront être sensibles au cycle de vue des View

        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(RecipeViewModel.class); // requireActivity(), donc c'est la
        // mm instance qu'on va partager entre différents fragments !!!
        viewModel.implementedFragmentListener(this); // just to say to ViewModel that its implemented Listener is in this Fragment

        binding.setViewmodel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureRecyclerView();
        if (getArguments() != null && !getArguments().isEmpty()) {
            args = RecipeFragmentArgs.fromBundle(getArguments());
            String[] ingredientArray = args.getIngredients();
            ingredients = Arrays.asList(ingredientArray); // Convert Array to List
            for (int i=0; i < ingredients.size() ; i++) {
                Log.d("RecipeFragment", "string : " + ingredients.get(i));
            }
        }
        this.loadDataWhenFragmentStarts();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    private void loadDataWhenFragmentStarts() {
        sharedPref = requireActivity().getSharedPreferences(FOR_BOTTOM_BAR, Context.MODE_PRIVATE);
        action = sharedPref.getString(getString(R.string.pref_bottom_view_action),"other");
        Log.d("RecipeFragment", "We are on : " + action);
        switch (action){
            case "research":
                viewModel.loadEdamamResearchData(ingredients,action);
                break;
            case "favorite":
                viewModel.loadFavoriteData(action);
                break;
            case "basket":
                binding.searchRestaurantButton.setVisibility(View.VISIBLE);
                viewModel.loadBasketData(action);
                break;
        }
    }

    @Override
    public void executeBindingMethods() {
        binding.setViewmodel(viewModel);
    }

    @Override
    public void navigateToDetail() {
        boolean isTablet = requireContext().getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            displayMasterDetailLayout();
        } else {
            displaySingleLayout();
        }
    }

    @Override
    public void navigateToRestaurant() {
        NavHostFragment.findNavController(this).navigate(RecipeFragmentDirections.actionRecipeFragmentToRestaurantFragment());
    }

    private void displaySingleLayout() {
        NavHostFragment.findNavController(this).navigate(RecipeFragmentDirections.actionRecipeFragmentToDetailFragment()); // S'utilise dans Fragment
    }

    private void displayMasterDetailLayout() {
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.profile_nav_container);
        // Ci-dessus, le NavHostFragment correspond au container (fragment) mis dans le fragment_recipe.xml !
        assert navHostFragment != null;
        navHostFragment.getNavController().navigate(R.id.detailFragment); // navHostFragment.findNavController(this).navigate(R.id.detailFragment);
    }

    private void configureRecyclerView(){
        //TODO
       binding.recipeRecyclerView.setAdapter(new RecipeAdapter(viewModel));
    }

}
