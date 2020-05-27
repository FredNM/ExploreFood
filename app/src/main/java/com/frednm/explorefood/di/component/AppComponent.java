package com.frednm.explorefood.di.component;

import android.app.Application;

import com.frednm.explorefood.App;
import com.frednm.explorefood.di.module.ActivityModule;
import com.frednm.explorefood.di.module.AppModule;
import com.frednm.explorefood.di.module.FragmentModule;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules={AndroidInjectionModule.class, ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(App app);
}
