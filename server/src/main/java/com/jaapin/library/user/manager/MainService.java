package com.jaapin.library.user.manager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.jaapin.library.user.IMainService;


public class MainService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return new IMainService.Stub() {
            @Override
            public String getUserName() throws RemoteException {
                return UserManager.getInstance().getUserName();
            }

            @Override
            public void setUserName(String name) throws RemoteException {
                UserManager.getInstance().setUserName(name);
            }
        };
    }
}
