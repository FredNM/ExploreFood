package com.frednm.explorefood.features.restaurant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frednm.explorefood.MainActivity;
import com.frednm.explorefood.R;
import com.frednm.explorefood.common.base.BaseFragment;
import com.frednm.explorefood.common.base.BaseViewModel;
import com.frednm.explorefood.data.model.Restaurant;
import com.frednm.explorefood.databinding.FragmentRestaurantBinding;
import com.frednm.explorefood.features.restaurant.view.RestaurantAdapter;

import javax.inject.Inject;

import static com.frednm.explorefood.MainActivity.FOR_LOCATION;

public class RestaurantFragment extends BaseFragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @NonNull
    private RestaurantViewModel viewModel;

    private FragmentRestaurantBinding binding;
    static SharedPreferences sharedPref;

    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner()); // For LiveData in View ! Les observer devront être sensibles au cycle de vue des View

        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(RestaurantViewModel.class);

        binding.setViewmodel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureRecyclerView();
        this.loadDataWhenFragmentStarts();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    private void loadDataWhenFragmentStarts() {
        ((MainActivity) requireActivity()).getLastKnownLocation(); // Re-exécute getLastKnowLocation pour récupérer la dernière position connue
        sharedPref = requireActivity().getSharedPreferences(FOR_LOCATION, Context.MODE_PRIVATE);
        double latitude = Double.longBitsToDouble(sharedPref.getLong(getString(R.string.pref_location_latitude),48));
        double longitude = Double.longBitsToDouble(sharedPref.getLong(getString(R.string.pref_location_longitude),2));
        Log.d("RestaurantFragment ", "Latitude : " + latitude + " Longitude : " + longitude);
        viewModel.loadZomatoResearchData(latitude,longitude);
    }

    private void configureRecyclerView(){
        binding.restaurantFragmentRecyclerView.setAdapter(new RestaurantAdapter());
    }
}
