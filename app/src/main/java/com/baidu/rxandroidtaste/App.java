package com.baidu.rxandroidtaste;

import android.app.Application;

import com.baidu.rxandroidtaste.dagger.CoffeeMaker;
import com.baidu.rxandroidtaste.dagger.DripCoffeeModule;
import com.baidu.rxandroidtaste.dagger.PumpModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gonggaofeng on 16/8/29;
 */

public class App extends Application {
    @Singleton
    @Component(modules = { DripCoffeeModule.class, PumpModule.class })
    public interface Coffee {
        CoffeeMaker maker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Coffee coffee = DaggerApp_Coffee.builder().build();
        coffee.maker().brew();
    }

}
