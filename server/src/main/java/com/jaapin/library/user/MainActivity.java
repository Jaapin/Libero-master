package com.jaapin.library.user;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.jaapin.library.libero.Libero;
import com.jaapin.library.user.manager.UserManager;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Libero.regiest(UserManager.class);

        UserManager.getInstance().setUserName("主进程设置用户：main");
    }

    public void getUserName(View view) {
        Toast.makeText(this, UserManager.getInstance().getUserName(), Toast.LENGTH_SHORT).show();
    }

    public void setUserName(View view) {
        UserManager.getInstance().setUserName("主进程设置用户：main");
    }

    public void goSub(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void goMain(View view) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.jaapin.library.client","MainActivity");
        intent.setComponent(componentName);
        startActivity(intent);
    }

}
