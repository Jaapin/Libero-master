package com.jaapin.library.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jaapin.library.libero.Libero;
import com.jaapin.library.user.manager.IUserManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Libero.connect(this);
    }

    public void getUserName(View view) {
        IUserManager iUserManager = Libero.getInstance(IUserManager.class);
        Toast.makeText(this, iUserManager.getUserName(), Toast.LENGTH_SHORT).show();
    }

    public void setUserName(View view) {
        IUserManager iUserManager = Libero.getInstance(IUserManager.class);
        iUserManager.setUserName("Client进程设置用户：Client");
    }

    public void go(View view) {
        finish();
    }
}
