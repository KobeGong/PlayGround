package com.baidu.rxandroidtaste.dagger;

import android.util.Log;

import javax.inject.Inject;

import dagger.Lazy;

public class CoffeeMaker {

  public static final String TAG = "coffee";

  private final Lazy<Heater> heater; // Create a possibly costly heater only when we use it.
  private final Pump pump;

  @Inject CoffeeMaker(Lazy<Heater> heater, Pump pump) {
    this.heater = heater;
    this.pump = pump;
  }

  public void brew() {
    heater.get().on();
    pump.pump();
    System.out.println(" [_]P coffee! [_]P ");
    heater.get().off();
    Log.d(TAG, "brew() called");
  }
}
