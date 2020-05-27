package com.frednm.explorefood.di.module;

import com.frednm.explorefood.MainActivity;
import com.frednm.explorefood.common.base.BaseActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract BaseActivity baseActivity();

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

}
