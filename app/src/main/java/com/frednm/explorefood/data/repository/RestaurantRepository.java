package com.frednm.explorefood.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.frednm.explorefood.data.local.RecipeDao;
import com.frednm.explorefood.data.local.RestaurantDao;
import com.frednm.explorefood.data.model.EdamamApiResult;
import com.frednm.explorefood.data.model.Recipe;
import com.frednm.explorefood.data.model.Restaurant;
import com.frednm.explorefood.data.model.ZomatoApiResult;
import com.frednm.explorefood.data.remote.RecipeDatasource;
import com.frednm.explorefood.data.remote.RestaurantDatasource;
import com.frednm.explorefood.data.repository.utils.ConnectivityLiveData;
import com.frednm.explorefood.data.repository.utils.NetworkBoundResource;
import com.frednm.explorefood.data.repository.utils.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class RestaurantRepository {

    private RestaurantDatasource restaurantDatasource;
    private RestaurantDao restaurantDao;
    private final ConnectivityLiveData connectivityLiveData;

    Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public RestaurantRepository(RestaurantDatasource restaurantDatasource, RestaurantDao restaurantDao, ConnectivityLiveData connectivityLiveData) {
        this.restaurantDatasource = restaurantDatasource;
        this.restaurantDao = restaurantDao;
        this.connectivityLiveData = connectivityLiveData;
    }

    public LiveData<Resource<List<Restaurant>>> getOnlineRestaurant(double latitude, double longitude){
        return new NetworkBoundResource<List<Restaurant>,ZomatoApiResult>(connectivityLiveData){
            @Override
            protected List<Restaurant> processResponse(ZomatoApiResult response) {
                List<Restaurant> restaurants = new ArrayList<>();
                if (response.getRestaurants() != null) {
                    for (int i = 0; i < response.getRestaurants().size(); i++) {
                        restaurants.add(response.getRestaurants().get(i).getRestaurant());
                    }
                    Log.d("RestaurantRepository", "Number of items " + response.getRestaurants().size());
                }
                return restaurants;
            }

            @Override
            protected void saveCallResults(@NonNull List<Restaurant> items) {
                restaurantDao.saveRestaurants(items); // Avant de sauvegarder les nouveaux résultats de la requête, enlève les anciens
                Log.d("RestaurantRepository", "Number of items saved " + items.size());
                //for (int i=0; i < items.size(); i++) {
                //    Log.d("RecipeRepository", "Label : " + items.get(i).getLabel());
                //}
            }

            @Override
            protected Boolean shouldFetch(@Nullable List<Restaurant> data) {
                return true ; // data == null || forceRefresh  ;
            }

            @NonNull
            @Override
            protected List<Restaurant> loadFromDb() {
                Log.d("RestaurantRepository", "Number of items from Db " + restaurantDao.getRestaurants().size());
                return restaurantDao.getRestaurants();
            }

            @NonNull
            @Override
            protected Call<ZomatoApiResult> createCallAsync() {
                return restaurantDatasource.fetchRestaurants(latitude, longitude);
            }
        }.asLiveData();
    }

}
