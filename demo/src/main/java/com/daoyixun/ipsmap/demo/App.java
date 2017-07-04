package com.daoyixun.ipsmap.demo;

import android.app.Application;

import com.daoyixun.ipsmap.IpsMapSDK;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IpsMapSDK.init(this, Constants.IPSMAP_APP_KEY);
    }
}
