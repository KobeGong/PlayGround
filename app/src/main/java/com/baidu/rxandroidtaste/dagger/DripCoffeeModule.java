package com.baidu.rxandroidtaste.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DripCoffeeModule {
  @Provides @Singleton Heater provideHeater() {
    return new ElectricHeater();
  }
}
