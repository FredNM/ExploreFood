package com.frednm.explorefood.features.recipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frednm.explorefood.R;
import com.frednm.explorefood.databinding.FragmentDetailRecipeBinding;
import com.frednm.explorefood.databinding.FragmentRecipeBinding;
import com.frednm.explorefood.features.recipe.utils.TextCreator;

import javax.inject.Inject;


public class DetailRecipeFragment extends Fragment implements RecipeViewModel.DetailListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @NonNull
    private RecipeViewModel viewModel;

    private FragmentDetailRecipeBinding binding;

    private TextCreator textCreator = new TextCreator();

    public static final String FOR_ORIENTATION = "FOR_ORIENTATION";

    public DetailRecipeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailRecipeFragment newInstance(String param1, String param2) {
        DetailRecipeFragment fragment = new DetailRecipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handleScreenOrientationChanges();
        // Inflate the layout for this fragment
        // TODO : in binding, setViewModel + Lifecycle, and textcreator
        binding = FragmentDetailRecipeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(RecipeViewModel.class); // requireActivity(), donc c'est la
        // mm instance qu'on va partager entre différents fragments !!!

        viewModel.implementedDetailFragmentListener(this);

        binding.setViewmodel(viewModel);
        binding.setTextCreator(textCreator);
        binding.setRecipe(viewModel.getRecipeData()); // On set directement le recipe ici !

        return binding.getRoot();
    }

    private void handleScreenOrientationChanges() {
        // Si en mode tablet, fait chemin inverse pour réafficher RecipeFragment avec ce DetailRecipeFragment à droite
        boolean isTablet = requireContext().getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (isTablet != requireActivity().getSharedPreferences(FOR_ORIENTATION, Context.MODE_PRIVATE).getBoolean(getString(R.string.pref_isTablet),false) ) {
                Log.d("DetailRecipeFragment", "Configuration changes to LANDSCAPE mode");
                updateSharedPreferences(isTablet);
                NavHostFragment.findNavController(this).popBackStack();
            }
        } else {
            updateSharedPreferences(isTablet);
        }
    }


    @Override
    public void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }else{
            //Page not found
        }
    }

    private void updateSharedPreferences(boolean isTablet) {
        // 1- save recipeName in SharedPreferences
        Log.d("DetailRecipeFragment", "In updateSharedPreferences : isTablet? " + isTablet);
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(FOR_ORIENTATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.pref_isTablet),isTablet);
        editor.apply();
    }
}
