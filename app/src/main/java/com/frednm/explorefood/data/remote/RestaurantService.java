package com.frednm.explorefood.data.remote;

import com.frednm.explorefood.data.model.ZomatoApiResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestaurantService {

    @Headers("user_key: 7167a641f66f55c67d172124f66fe507") // Quasi obligatoire de mettre clé dans Headers https://stackoverflow.com/questions/45090181/zomato-api-integration-with-in-android-app
    @GET("api/v2.1/search")
    public Call<ZomatoApiResult> fetchRestaurants(
                                               @Query("lat") Double lat,
                                               @Query("lon") Double lon,
                                               @Query("radius") Double radius,
                                               @Query("sort") String sort );
}
