package com.frednm.explorefood.data.repository.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * I added the possibility of network missing.
 * @param <T>
 */

public class Resource<T> {

    private Status status;
    private @Nullable T data;
    private Throwable error;


    public Resource(Status status, @Nullable T data, Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    // -- Getters, to be called out of the class
    public Status getStatus() { return status; }
    @Nullable
    public T getData() { return data; }
    public Throwable getError() { return error; }

    // Read this https://stackoverflow.com/questions/39657627/how-to-pass-generic-object-as-a-generic-parameter-on-other-method-in-java
    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource(
                Status.SUCCESS,
                data,
                null
        );
    }

    public static <T> Resource<T> error(Throwable error, @Nullable T data) {
        return new Resource(
                Status.ERROR,
                data,
                error
        );
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource(
                Status.LOADING,
                data,
                null
        );
    }

    public static <T> Resource<T> noNetwork(@Nullable T data) {
        return new Resource(
                Status.NONETWORK,
                data,
                null
        );
    }


    /**
     * Utils function, just to be able to handle declaration site-variance if needed.
     */
    static <T> Resource<T> narrow(Resource<? extends T> resource) {
        return (Resource<T>) resource;
    }

    public enum  Status {
        SUCCESS,
        ERROR,
        LOADING,
        NONETWORK
    }
}


