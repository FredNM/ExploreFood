package com.frednm.explorefood.data.repository.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * COMPLETLY USELESS HERE! JUST TO MEET THE UDACITY REQUIREMENT!
 * The doInBackground() method implemented where MyAsyncTask is called will have to return a T type.
 * This T type will also have to be input of the onPostExectute() method !
 * @param <T>
 */

public class MyAsyncTask<T> extends AsyncTask<Void, Void, T> {

    // 1 - Implement listeners methods (Callback)
    public interface Listeners<T> {
        void onPreExecute();
        T doInBackground();
        void onPostExecute(T success);
    }

    // 2 - Declare callback
    private final WeakReference<Listeners<T>> callback;

    // 3 - Constructor
    public MyAsyncTask(Listeners<T> callback){
        this.callback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.callback.get().onPreExecute(); // 4 - Call the related callback method
        Log.e("TAG", "AsyncTask is started.");
    }

    @Override
    protected void onPostExecute(T success) {
        super.onPostExecute(success);
        this.callback.get().onPostExecute(success); // 4 - Call the related callback method
        Log.e("TAG", "AsyncTask is finished.");
    }

    @Override
    protected T doInBackground(Void... voids) {
        Log.e("TAG", "AsyncTask doing some big work...");
        return this.callback.get().doInBackground(); // 4 - Call the related callback method
    }
}

// HOW IT CAN BE IMPLEMENTED IN ANOTHER CLASS, IMPLEMENTATION WITH INNER INTERFACE
//        new MyAsyncTask<String>(
//        new MyAsyncTask.Listeners<String>() {
//@Override
//public void onPreExecute() { }
//@Override
//public String doInBackground() {
//        // DO JOB
//        return "Good job!";
//        }
//@Override
//public void onPostExecute(String success) { }
//        }).execute(); // Could use get() instead of execute() in order to set timeout, but get() run on UI thread !