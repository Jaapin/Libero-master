package com.jaapin.library.libero;

import android.util.Log;

import com.google.gson.Gson;
import com.jaapin.library.libero.model.Request;
import com.jaapin.library.libero.model.Response;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class LiberoInvocationHandler implements InvocationHandler {
    private static final String TAG = "xiaolu";

    private Class<?> mInstanceClass;
    private Class<? extends LiberoService> mService;
    private Gson gson = new Gson();

    public <T> LiberoInvocationHandler(Class<?> instanceClass, Class<? extends LiberoService> service) {
        mInstanceClass = instanceClass;
        mService = service;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Log.d(TAG, "invoke: " + method.getName());
        //再去请求服务端执行真正的方法
        Response response = Channel.getInstance().send(Request.GET_METHOD, mService, mInstanceClass, method.getName(), objects);
        if (response.isSuccess()) {
            Class<?> returnType = method.getReturnType();
            if (returnType != Void.class && returnType != void.class) {
                return gson.fromJson(response.getSource(), returnType);
            }
        }
        return null;
    }
}
