package com.frednm.explorefood.di.module;


import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.frednm.explorefood.App;
import com.frednm.explorefood.data.local.RecipeAppDatabase;
import com.frednm.explorefood.data.local.RecipeDao;
import com.frednm.explorefood.data.local.RestaurantDao;
import com.frednm.explorefood.data.remote.RecipeDatasource;
import com.frednm.explorefood.data.remote.RecipeService;

import com.frednm.explorefood.data.remote.RestaurantDatasource;
import com.frednm.explorefood.data.remote.RestaurantService;
import com.frednm.explorefood.data.repository.RecipeRepository;
import com.frednm.explorefood.data.repository.RestaurantRepository;
import com.frednm.explorefood.data.repository.utils.ConnectivityLiveData;
import com.frednm.explorefood.di.qualifiers.ForApplication;
import com.frednm.explorefood.di.qualifiers.ForZomato;
import com.frednm.explorefood.features.recipe.domain.GetBasketRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.GetFavoriteRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.GetOnlineRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.UpdateBasketRecipesUseCase;
import com.frednm.explorefood.features.recipe.domain.UpdateFavoriteRecipesUseCase;
import com.frednm.explorefood.features.restaurant.domain.GetRestaurantsUseCase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

/**    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    } */

    // --- DATABASE INJECTION ---
    @Provides
    @Singleton
    RecipeAppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                RecipeAppDatabase.class, "RecipeAppDatabase.db")
                .build();
    }

    @Provides
    @Singleton
    RecipeDao provideRecipeDao(RecipeAppDatabase database) { return database.recipeDao(); }

    @Provides
    @Singleton
    RestaurantDao provideRestaurantDao(RecipeAppDatabase database) { return database.restaurantDao(); }


    // --- NETWORK INJECTION ---

    private static String EDAMAM_BASE_URL = "https://api.edamam.com/";
    private static String ZOMATO_BASE_URL = "https://developers.zomato.com/";

    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

    @Provides
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        return okHttpClient;
    }

    // 1- --- Case of EDAMAM
    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(EDAMAM_BASE_URL)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    RecipeService provideRecipeService(Retrofit restAdapter) {
        return restAdapter.create(RecipeService.class);
    }

    @Provides
    RecipeDatasource provideRecipeDatasource(RecipeService recipeService) {
        return new RecipeDatasource(recipeService);
    }

    // 2- --- Case of EDAMAM
    @Provides
    @ForZomato
    Retrofit provideZomatoRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ZOMATO_BASE_URL)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    RestaurantService provideRestaurantService(@ForZomato Retrofit restAdapter) {
        return restAdapter.create(RestaurantService.class);
    }

    @Provides
    RestaurantDatasource provideRestaurantDatasource(RestaurantService restaurantService) {
        return new RestaurantDatasource(restaurantService);
    }

    // --- REPOSITORY INJECTIONS ---
    @Provides
    @ForApplication
    Context providesApplicationContext(Application application) {
        return application;
    }

    //@Singleton
    @Provides
    ConnectivityLiveData providesConnectivityLiveData(@ForApplication Context context) {
        return new ConnectivityLiveData(context);
    }

    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(RecipeDatasource recipeDatasource, RecipeDao recipeDao, ConnectivityLiveData connectivityLiveData) {
        return new RecipeRepository(recipeDatasource, recipeDao, connectivityLiveData);
    }

    @Provides
    @Singleton
    RestaurantRepository provideRestaurantRepository(RestaurantDatasource restaurantDatasource, RestaurantDao restaurantDao, ConnectivityLiveData connectivityLiveData) {
        return new RestaurantRepository(restaurantDatasource, restaurantDao, connectivityLiveData);
    }

    // --- DOMAIN INJECTIONS ---
    @Provides
    GetOnlineRecipesUseCase provideGetOnlineRecipesUseCase(RecipeRepository recipeRepository) {
        return new GetOnlineRecipesUseCase(recipeRepository);
    }

    @Provides
    GetFavoriteRecipesUseCase provideGetFavoriteRecipesUseCase(RecipeRepository recipeRepository) {
        return new GetFavoriteRecipesUseCase(recipeRepository);
    }

    @Provides
    GetBasketRecipesUseCase provideGetBasketRecipesUseCase(RecipeRepository recipeRepository) {
        return new GetBasketRecipesUseCase(recipeRepository);
    }

    @Provides
    UpdateFavoriteRecipesUseCase provideUpdateFavoriteRecipesUseCase(RecipeRepository recipeRepository) {
        return new UpdateFavoriteRecipesUseCase(recipeRepository);
    }

    @Provides
    UpdateBasketRecipesUseCase provideUpdateBasketRecipesUseCase(RecipeRepository recipeRepository) {
        return new UpdateBasketRecipesUseCase(recipeRepository);
    }

    @Provides
    GetRestaurantsUseCase provideGetRestaurantsUseCase(RestaurantRepository restaurantRepository) {
        return new GetRestaurantsUseCase(restaurantRepository);
    }

}

