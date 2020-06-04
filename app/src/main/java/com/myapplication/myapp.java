package com.myapplication;

import android.app.Application;
import android.widget.Toast;

import com.xuexiang.xui.XUI;

public class myapp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志

//        Toast.makeText(this,"myapp",Toast.LENGTH_SHORT).show();

    }
}
