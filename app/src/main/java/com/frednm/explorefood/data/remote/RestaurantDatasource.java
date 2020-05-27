package com.frednm.explorefood.data.remote;

import android.util.Log;

import com.frednm.explorefood.data.model.EdamamApiResult;
import com.frednm.explorefood.data.model.ZomatoApiResult;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class RestaurantDatasource {

    //private static final String appKey = "7167a641f66f55c67d172124f66fe507"; // TODO Put your API key here !
    private RestaurantService restaurantService ;
    private String sort = "rating"; // sort peut valoir cost ou rating ou real_distance (https://developers.zomato.com/documentation#!/restaurant/search)
    private double radius = 10000.00; // 10000 m, 10 km !

    @Inject
    public RestaurantDatasource(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public Call<ZomatoApiResult> fetchRestaurants(double latitude, double longitude){
        Log.d("Recipe DataSource", "Fetching recipes");
        return restaurantService.fetchRestaurants(latitude,longitude,radius, sort);
    }
}
