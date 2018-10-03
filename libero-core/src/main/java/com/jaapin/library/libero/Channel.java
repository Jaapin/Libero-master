package com.jaapin.library.libero;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jaapin.library.libero.anonotation.ClassId;
import com.jaapin.library.libero.model.Parameters;
import com.jaapin.library.libero.model.Request;
import com.jaapin.library.libero.model.Response;


import java.util.concurrent.ConcurrentHashMap;

public class Channel {

    private static final String TAG = "Channel";

    private static volatile Channel instance;
    private ConcurrentHashMap<Class<? extends LiberoService>, Boolean> mBound = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<? extends LiberoService>, LiberoServiceConnection> mLiberoServices = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<? extends LiberoService>, ILiberoService> mILibreos = new ConcurrentHashMap<>();

    Gson gson = new Gson();

    private Channel() {
    }

    public static Channel getInstance() {
        if (null == instance) {
            synchronized (Channel.class) {
                if (null == instance) {
                    instance = new Channel();
                }
            }
        }
        return instance;
    }

    public void bind(Context context, String pkgName, Class<? extends LiberoService> service) {
        //已经绑定了，就不再绑定
        Boolean isBound = mBound.get(service);
        if (null != isBound && isBound) {
            return;
        }

        Intent intent;
        if (TextUtils.isEmpty(pkgName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(pkgName, service.getName());
        }
        LiberoServiceConnection LiberoServiceConnection = new LiberoServiceConnection(service);
        mLiberoServices.put(service, LiberoServiceConnection);
        context.bindService(intent, LiberoServiceConnection, Context.BIND_AUTO_CREATE);

    }

    public void unBind(Context context, Class<? extends LiberoService> service) {
        Boolean isBound = mBound.get(service);
        if (null == isBound || !isBound) {
            return;
        }

        LiberoServiceConnection liberoServiceConnection = mLiberoServices.remove(service);
        if (null != liberoServiceConnection) {
            context.unbindService(liberoServiceConnection);
        }
    }

    public Response send(int type, Class<? extends LiberoService> service, Class<?> instanceClass, String methodName, Object... parameters) {
        Boolean isBound = mBound.get(service);
        if (null == isBound || !isBound) {
            return new Response(null, "服务未连接", false);
        }

        //获得classId
        ClassId annotation = instanceClass.getAnnotation(ClassId.class);
        String classId;
        if (null != annotation) {
            classId = annotation.value();
        } else {
            classId = instanceClass.getName();
        }
        Parameters[] parametersList = makeParameters(parameters);
        Request request = new Request(type, classId, methodName, parametersList);
        ILiberoService iLiberoService = mILibreos.get(service);
        try {
            if (iLiberoService != null) {
                return iLiberoService.send(request);
            } else {
                throw new RuntimeException();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Parameters[] makeParameters(Object[] parameters) {
        Parameters[] p;
        if (null != parameters) {
            p = new Parameters[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object object = parameters[i];
                p[i] = new Parameters(object.getClass().getName(), gson.toJson(object));
            }
        } else {
            p = new Parameters[0];
        }
        return p;
    }


    class LiberoServiceConnection implements ServiceConnection {

        private final Class<? extends LiberoService> mService;

        LiberoServiceConnection(Class<? extends LiberoService> service) {
            this.mService = service;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            ILiberoService iLiberoService = ILiberoService.Stub.asInterface(iBinder);
            mILibreos.put(mService, iLiberoService);
            mBound.put(mService, true);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected: ");
            mILibreos.remove(mService);
            mBound.put(mService, false);
        }
    }
}
