package com.frednm.explorefood;

import android.app.Application;
import android.content.Context;

import com.frednm.explorefood.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class App extends Application implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> activityDispatchingInjector;

    public Context context; // TODO Check: is there a problem ? It was static before !?!

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return  activityDispatchingInjector;
    }

    // ---

    private void initDagger(){
        DaggerAppComponent.builder().application(this).build().inject(this);
    }
}
