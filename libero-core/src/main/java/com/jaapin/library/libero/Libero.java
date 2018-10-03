package com.jaapin.library.libero;

import android.content.Context;
import android.util.Log;


import com.jaapin.library.libero.model.Request;
import com.jaapin.library.libero.model.Response;

import java.lang.reflect.Proxy;

/**
 * 对外提供的API接口
 */
public class Libero {

    private static final String TAG = "Libero";

    //服务端
    //注册 允许客户端调用的接口
    public static void regiest(Class<?> service) {
        TypeCenter.getInstance().regiest(service);
    }

    //客户端
    public static void connect(Context context) {
        connect(context, null, LiberoService.LiberoService0.class);
    }

    public static void connect(Context context, Class<? extends LiberoService> service) {
        connect(context, null, service);
    }

    public static void connect(Context context, String pkgName) {
        connect(context, pkgName, LiberoService.LiberoService0.class);
    }

    public static void connect(Context context, String pkgName, Class<? extends LiberoService> service) {
        Channel.getInstance().bind(context, pkgName, service);
    }

    public static void disconnect(Context context, Class<? extends LiberoService> service) {
        Channel.getInstance().unBind(context.getApplicationContext(), service);
    }

    public static <T> T getInstance(Class<T> instanceClass, Object... parameters) {
        return getInstance(instanceClass, LiberoService.LiberoService0.class, parameters);
    }

    public static <T> T getInstance(Class<T> instanceClass, Class<? extends LiberoService> service, Object... parameters) {
        return getInstance(instanceClass, "getInstance", service, parameters);
    }

    public static <T> T getInstance(Class<T> instanceClass, String methodName, Class<? extends LiberoService> service, Object... parameters) {
        Response response = Channel.getInstance().send(Request.GET_INSTANCE, service, instanceClass, methodName, parameters);
        if (response.isSuccess()) {
            return getProxy(instanceClass, service);
        }
        Log.e(TAG, response.getMessage());
        return null;
    }

    static <T> T getProxy(Class<T> instanceClass, Class<? extends LiberoService> service) {
        return (T) Proxy.newProxyInstance(instanceClass.getClassLoader(), new Class[]{instanceClass}, new LiberoInvocationHandler(instanceClass, service));
    }

}
