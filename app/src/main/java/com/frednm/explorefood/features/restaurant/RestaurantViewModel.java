package com.frednm.explorefood.features.restaurant;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.frednm.explorefood.R;
import com.frednm.explorefood.common.base.BaseViewModel;
import com.frednm.explorefood.common.utils.Event;
import com.frednm.explorefood.data.model.Restaurant;
import com.frednm.explorefood.data.repository.utils.Resource;
import com.frednm.explorefood.features.restaurant.domain.GetRestaurantsUseCase;

import java.util.List;

import javax.inject.Inject;

public class RestaurantViewModel extends BaseViewModel {

    private GetRestaurantsUseCase getRestaurantsUseCase;

    // --- For data shown on RecipeFragment
    private MediatorLiveData<Resource<List<Restaurant>>> _restaurants = new MediatorLiveData<>();
    public LiveData<Resource<List<Restaurant>>> restaurants = _restaurants;
    private LiveData<Resource<List<Restaurant>>> restaurantsSource = new MutableLiveData<>();

    // --- CONSTRUCTOR
    @Inject
    public RestaurantViewModel(GetRestaurantsUseCase getRestaurantsUseCase) {
        this.getRestaurantsUseCase = getRestaurantsUseCase;
    }

    public void loadZomatoResearchData(double latitude, double longitude){
        getOnlineRestaurants(latitude, longitude);
    }

    private void getOnlineRestaurants(double latitude, double longitude) {
        _restaurants.removeSource(restaurantsSource);
        restaurantsSource = getRestaurantsUseCase.invoke(latitude, longitude);
        _restaurants.addSource(restaurantsSource, this::treatData);
    }

    private void treatData(Resource<List<Restaurant>> resource) {
        this._restaurants.setValue(resource);
        if(resource.getData() != null) {
            Log.d("RestaurantViewModel", "Number of data : " + resource.getData().size());
        } else {
            Log.d("RestaurantViewModel", "Null data : ");
        }
        if (resource.getStatus() == Resource.Status.ERROR) _snackbarError.setValue(new Event<>(R.string.an_error_happened));
        if (resource.getStatus() == Resource.Status.NONETWORK) _snackbarError.setValue(new Event<>(R.string.no_network));
    }
}
