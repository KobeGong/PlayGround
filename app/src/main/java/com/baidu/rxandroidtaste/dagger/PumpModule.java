package com.baidu.rxandroidtaste.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class PumpModule {
  @Provides Pump providePump(Thermosiphon pump) {
    return pump;
  }
}
