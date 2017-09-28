package com.daoyixun.ipsmap.ipslocation;

import android.app.Application;

import com.daoyixun.location.ipsmap.IpsLocationSDK;


/**
 * author:chen
 * time:2017/9/26
 * desc:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IpsLocationSDK.init(new IpsLocationSDK.Configuration.Builder(this)
                .appKey(Constants.IPSMAP_APP_KEY)
                .debug(true)
                .build());
    }
}
