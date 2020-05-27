/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.frednm.explorefood.data.repository.utils;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Data display to UI are always taken from the DB. Even when a new network request is performed, data are first saved in DB
 * (as we can see it on the fetchFromNetwork() method), and then taken from the DB.
 * The DB therefore acts as the Single Source of Truth.
 */

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final MutableLiveData<Resource<ResultType>> result = new MutableLiveData<>();

    Executor executor = Executors.newSingleThreadExecutor();
    private final ConnectivityLiveData connectivityLiveData;

    @MainThread
    protected NetworkBoundResource(ConnectivityLiveData connectivityLiveData) {
        this.connectivityLiveData = connectivityLiveData;
        init();
    }

    private void init(){
        result.setValue(Resource.loading(null));
        executor.execute(() -> {
            ResultType dbResult = loadFromDb();
            if (shouldFetch(dbResult)) {
                if(connectivityLiveData.getValue()) {
                    try {
                        fetchFromNetwork(dbResult);
                    } catch (Exception e) {
                        Log.e("NetworkBoundResource", "An error happened: "+e);
                        setValue(Resource.error(e, loadFromDb()));
                    }
                } else {
                    Log.d("NetworkBoundResource", "No network");
                    setValue(Resource.noNetwork(dbResult));
                }
            } else {
                Log.d("NetworkBoundResource", "Return data from local database");
                setValue(Resource.success(dbResult));
            }
        });
    }

    // BAD VERSION of fetchFromNetwork ! I use AsyncTask just to meet Udacity requirements, but it's completly useless !
    private void fetchFromNetwork(final ResultType dbResult) { // dbResult type depends on type return by loadFromDb()
            new MyAsyncTask<String>(
                    new MyAsyncTask.Listeners<String>() {
                        @Override
                        public void onPreExecute() { }
                        @Override
                        public String doInBackground() {
                            setValue(Resource.loading(dbResult)); // Dispatch latest value quickly (for UX purpose)
                            Log.d("NetworkBoundResource", "Entering in fetchFromNewtwork");
                            createCallAsync().enqueue(new Callback<RequestType>() {
                                @Override
                                public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                                    executor.execute(()-> {
                                        Log.d("NetworkBoundResource", "Entering in onResponse");
                                        saveCallResults(processResponse(response.body())); // Save result in DB
                                        setValue(Resource.success(loadFromDb()));//Take new data from DB
                                        Log.d("NetworkBoundResource", "Exiting from onResponse");
                                    });
                                }

                                @Override
                                public void onFailure(Call<RequestType> call, Throwable t) {
                                    Log.e("NetworkBoundResource", "An error happened during NetworkRequest:"+t.getMessage());
                                    setValue(Resource.error(t, dbResult));
                                }
                            });
                            return "Good job!";
                        }
                        @Override
                        public void onPostExecute(String success) { }
                    }).execute();

    }

    // Good version of fetchFromNetwork !
    /** private void fetchFromNetwork(final ResultType dbResult) {
        setValue(Resource.loading(dbResult)); // Dispatch latest value quickly (for UX purpose)
        Log.d("NetworkBoundResource", "Entering in fetchFromNewtwork");
        createCallAsync().enqueue(new Callback<RequestType>() {
            @Override
            public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                executor.execute(()-> {
                    Log.d("NetworkBoundResource", "Entering in onResponse");
                    saveCallResults(processResponse(response.body())); // Save result in DB
                    setValue(Resource.success(loadFromDb()));//Take new data from DB
                    Log.d("NetworkBoundResource", "Exiting from onResponse");
                });
            }

            @Override
            public void onFailure(Call<RequestType> call, Throwable t) {
                Log.e("NetworkBoundResource", "An error happened during NetworkRequest:"+t.getMessage());
                setValue(Resource.error(t, dbResult));
            }
        });
    } */

    /**
     * This method is responsible to do Resource.success(), Resource.error(), Resource.loading(). Therefore, it's the method giving
     * the data that will be displayed on the UI.
     */
    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        Log.d("NetworkBoundResource", "Resource: "+newValue);
        if (result.getValue() != newValue) result.postValue(newValue);
    }

    public final LiveData<Resource<ResultType>> asLiveData() { return result;  }

    @WorkerThread
    protected abstract ResultType processResponse(RequestType response);

    @WorkerThread
    protected abstract void saveCallResults(@NonNull ResultType items);

    @MainThread
    protected abstract Boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract ResultType loadFromDb();

    @NonNull
    @MainThread
    protected abstract Call<RequestType> createCallAsync();

}
