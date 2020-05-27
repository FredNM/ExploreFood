package com.frednm.explorefood.features.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.frednm.explorefood.R;
import com.frednm.explorefood.databinding.FragmentHomeBinding;
import com.frednm.explorefood.features.widget.UpdateIngredientService;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements IngredientsAdapter.Listener{

    private FragmentHomeBinding binding;
    private Button addButton;
    private Button clearButton;
    private Button searchButton;
    private EditText ingredientInput;
    private RecyclerView recyclerView;
    //private ChipGroup cuisineType;
    private List<String> ingredients = new ArrayList<>();

    private IngredientsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        addButton = binding.addButton;
        clearButton = binding.clearButton;
        searchButton = binding.searchButton;
        ingredientInput = binding.ingredientInput;
        recyclerView = binding.ingredientsList;
       // cuisineType  = binding.recipeCuisineType;
        // Inflate the layout for this fragment
        addButton.setOnClickListener((view) -> addIngredient()); // TODO le onClickAddButton plus bas, à comparer
        clearButton.setOnClickListener((view) -> clear());
        searchButton.setOnClickListener((view) -> navigateToRecipeFragment());
        ingredientInput.setOnClickListener((view) -> activateAddButton());
        // cuisineType.setOnClickListener((view) -> handleSelectedChips());

        this.configureRecyclerView();

        this.setUpWidgetServiceMethod(); // TODO test if work

        return binding.getRoot();
    }

    // -------------------
    // ACTIONS
    // -------------------

    private void addIngredient() {
        ingredients.add(binding.ingredientInput.getText().toString());
        binding.ingredientInput.getText().clear(); // Supprime ce que l'utilisateur avait déjà rempli
        this.updateItemList(ingredients);
        addButton.setEnabled(false); // Désactive bouton d'ajout après l'ajout
        hideKeyboard(requireContext(),binding.ingredientInput);
    }

    private void clear() {
        ingredients.clear();
        this.updateItemList(ingredients);
    }

    @Override
    public void onClickDeleteButton(int position) {
        ingredients.remove(position);
        this.updateItemList(ingredients);
    }

    private void handleSelectedChips() {
        //TODO
    }

    private void navigateToRecipeFragment() {
        // TODO Passer la liste d'ingrédients et de selected chips puis naviguer vers RecipeFragment
        // Convert List to Array
        String[] ingredientsArray = new String[ingredients.size()];
        ingredientsArray = ingredients.toArray(ingredientsArray);
        // Now pass the array
        NavHostFragment.findNavController(this).navigate(HomeFragmentDirections.actionHomeFragmentToRecipeFragment(ingredientsArray));
    }

    // -------------------
    // OTHERS
    // -------------------
    private void configureRecyclerView(){
        this.adapter = new IngredientsAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
    }

    private void activateAddButton(){
        this.ingredientInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ingredientInput.length()>= 1) {
                    addButton.setEnabled(true);
                } else {
                    addButton.setEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void updateItemList(List<String> ingredients){
        this.adapter.updateData(ingredients);
    }

    // Taken here https://stackoverflow.com/questions/44450642/cant-hide-soft-input-keyboard-getwindowtoken-always-null
    private void hideKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // ---- WIDGET
    private void setUpWidgetServiceMethod(){
        //String ingredientText = viewModel.getIngredientText();
        this.executeWidgetServiceMethod("testeur");
    }

    private void executeWidgetServiceMethod(@NonNull String ingredientText) {
        ArrayList<String> list = new ArrayList<>();
        list.add(ingredientText);
        UpdateIngredientService.startBakingService(requireContext(),list);
    }

}


