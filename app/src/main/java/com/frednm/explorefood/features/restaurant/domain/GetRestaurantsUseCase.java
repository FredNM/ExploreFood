package com.frednm.explorefood.features.restaurant.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.frednm.explorefood.data.model.Restaurant;
import com.frednm.explorefood.data.repository.RestaurantRepository;
import com.frednm.explorefood.data.repository.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class GetRestaurantsUseCase {

    public RestaurantRepository repository;

    @Inject
    public GetRestaurantsUseCase(RestaurantRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<Restaurant>>> invoke(double latitude, double longitude){
        return Transformations.map(repository.getOnlineRestaurant(latitude, longitude),this::transform);
    }

    // Useless so far, but could be interesting to apply something here
    private Resource<List<Restaurant>> transform(Resource<List<Restaurant>> data) {
        return data;
    }
}
