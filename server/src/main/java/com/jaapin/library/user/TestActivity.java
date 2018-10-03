package com.jaapin.library.user;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.jaapin.library.user.manager.MainService;

/**
 * 按照aidl的方式
 */

public class TestActivity extends Activity {

    private ServiceConnection serviceConnection;

    IMainService iMainService;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iMainService = IMainService.Stub.asInterface(service);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(new Intent(this, MainService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    public void getUserName(View view) {
        try {
            Toast.makeText(this, iMainService.getUserName(), Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setUserName(View view) {
        try {
            iMainService.setUserName("Test进程设置用户：test");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void go(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
