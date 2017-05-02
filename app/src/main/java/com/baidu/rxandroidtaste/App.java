package com.baidu.rxandroidtaste;

import android.app.Application;
import android.content.Context;

import com.baidu.rxandroidtaste.dagger.CoffeeMaker;
import com.baidu.rxandroidtaste.dagger.DripCoffeeModule;
import com.baidu.rxandroidtaste.dagger.PumpModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        makeProxyForWindowManager();
    }

    private void makeProxyForWindowManager() {
        Object targetWm = CommonUtils.invokeNoParamsMethod("android.view.WindowManagerGlobal", "getWindowManagerService", null);
        CommonUtils.checkNull(targetWm, "targetWm can not be null");
        Class[] interfaces = CommonUtils.collectInterfaces(targetWm.getClass());
        Object proxyWm = Proxy.newProxyInstance(targetWm.getClass().getClassLoader(), interfaces, new FakeWindowManager(targetWm));
        CommonUtils.setFieldValue("android.view.WindowManagerGlobal", "sDefaultWindowManager", null, proxyWm);
    }

    public static class FakeWindowManager implements InvocationHandler {

        Object realWindowManager;

        public FakeWindowManager(Object realWindowManager) {
            this.realWindowManager = realWindowManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return CommonUtils.invokeMethod(method, realWindowManager, args);
        }
    }
}
