package com.jaapin.library.libero;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.jaapin.library.libero.model.Parameters;
import com.jaapin.library.libero.model.Request;
import com.jaapin.library.libero.model.Response;


import java.lang.reflect.Method;

public abstract class LiberoService extends Service {

    private static final String TAG = "LiberoService";

    private TypeCenter typeCenter = TypeCenter.getInstance();
    private Gson gson = new Gson();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ILiberoService.Stub() {
            @Override
            public Response send(Request request) throws RemoteException {
                switch (request.getType()) {
                    case Request.GET_INSTANCE: {
                        //单例
                        //参数
                        Object[] objects = restoreParameters(request.getParameters());
                        //调用的类型
                        Class<?> clazz = typeCenter.getClassType(request.getClassType());
                        //调用的方法
                        Method method = typeCenter.getMethod(clazz, request.getMethodName());
                        try {
                            //调用
                            Object instance = method.invoke(null, objects);
                            //记住这个单例
                            typeCenter.putObject(request.getClassType(), instance);
                            return new Response(null, null, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Response(null, e.getMessage(), false);
                        }
                    }
                    case Request.GET_METHOD:
                        Object[] objects = restoreParameters(request.getParameters());
                        Class<?> clazz = typeCenter.getClassType(request.getClassType());
                        Method method = typeCenter.getMethod(clazz, request.getMethodName());
                        Object object = typeCenter.getObject(request.getClassType());
                        try {
                            Object returnObj = method.invoke(object, objects);
                            String source = gson.toJson(returnObj);
                            Response response = new Response(source, null, true);
                            Log.i(TAG, "source: " + source);
                            Log.i(TAG, "response: " + response.toString());
                            return response;
                        } catch (Exception e) {
                            return new Response(null, e.getMessage(), false);
                        }
                }
                return null;
            }
        };
    }

    protected Object[] restoreParameters(Parameters[] parameters) {
        Object[] objects;
        if (null != parameters && parameters.length > 0) {
            objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameters p = parameters[i];
                objects[i] = gson.fromJson(p.getValue(), typeCenter.getClassType(p.getType()));
            }
        } else {
            objects = new Object[0];
        }
        return objects;
    }


    public static class LiberoService0 extends LiberoService {
    }

    public static class LiberoService1 extends LiberoService {
    }

    public static class LiberoService2 extends LiberoService {
    }

}
