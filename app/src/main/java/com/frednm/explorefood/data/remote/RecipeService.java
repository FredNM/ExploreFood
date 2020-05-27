package com.frednm.explorefood.data.remote;

import com.frednm.explorefood.data.model.EdamamApiResult;
import com.frednm.explorefood.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

    @GET("search")
    public Call<EdamamApiResult> fetchRecipes( // EdamamApiResult<Recipe>
            @Query("q")List<String> q,
            @Query("_app_id")String appId,
            @Query("_app_key")String appKey );
}
