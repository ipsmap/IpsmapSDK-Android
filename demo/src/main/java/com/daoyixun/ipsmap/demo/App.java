package com.daoyixun.ipsmap.demo;

import android.app.Application;

import com.daoyixun.ipsmap.IpsMapSDK;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IpsMapSDK.init(new IpsMapSDK.Configuration.Builder(this)
                .appKey(Constants.IPSMAP_APP_KEY)
                .enableShowShareDialog(true)
                .build());
    }
}
