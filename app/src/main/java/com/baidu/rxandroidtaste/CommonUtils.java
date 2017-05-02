package com.baidu.rxandroidtaste;

import android.content.res.Resources;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by gonggaofeng on 15/12/15;
 */
public class CommonUtils {

    public static float dpToPx(int dp){
        return Resources.getSystem().getDisplayMetrics().density * dp;
    }

    public static Class<?>[] collectInterfaces(Class<?> clazz) {
        Set<Class> interfaces = new LinkedHashSet<>();
        while (clazz != null) {
            Class<?>[] subInterfaces = clazz.getInterfaces();
            if (subInterfaces != null && subInterfaces.length > 0) {
                Collections.addAll(interfaces, subInterfaces);
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces.toArray(new Class[interfaces.size()]);
    }

    public static Object invokeNoParamsMethod(String className, String methodName, Object object) {
        try {
            Class<?> clazz = Class.forName(className);
            Method m = clazz.getDeclaredMethod(methodName);
            m.setAccessible(true);
            return m.invoke(object);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(String className, String fieldName, Object o, Object newValue) {
        try {
            Class<?> clazz = Class.forName(className);
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(o, newValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void checkNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }

    public static Object invokeMethod(Method method, Object target, Object... parms) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(target, method);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
