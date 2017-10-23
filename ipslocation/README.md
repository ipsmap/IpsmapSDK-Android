# IpsmapSDK-Android

[![license](https://img.shields.io/hexpm/l/plug.svg)](https://raw.githubusercontent.com/typ0520/fastdex/master/LICENSE)
[![Download](https://api.bintray.com/packages/xun/maven/com.ipsmap/images/download.svg) ](https://bintray.com/xun/maven/com.ipsmap/_latestVersion)
[![API](https://img.shields.io/badge/API-18%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=18)
[![Contact](https://img.shields.io/badge/Author-IpsMap-orange.svg?style=flat)](http://ipsmap.com)

Ipslocation-Android 是一套基于 Android 4.3 及以上版本的室内地图应用程序开发接口，供开发者在自己的Android应用中加入室内地图相关的功能，包括：获取当前位置等功能。

## 获取AppKey和MapId
请联系dev@ipsmap.com

## 添加依赖

```
注意如果同时使用了ipsmap的导航模块则不用导入,ispmap 导航模块已经导入了ips-location 模块

compile ('com.ipsmap:ips-location:0.2.0', {
        exclude group: 'com.android.support'
    })
```

## 加入权限
导入IpsmapSDK后需要
```
    <!-- sdk 使用需要的权限 -->
    <!-- if use wifi indoor positioning -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <!-- if use ble indoor positioning -->
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <!-- general permission -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <!-- 连接网络权限，用于执行云端语音能力 -->
    <!-- 获取手机录音机使用权限，听写、识别、语义理解需要用到此权限 -->
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <!-- 读取网络信息状态 -->
    <!-- 允许程序改变网络连接状态 -->
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
    <!-- 读取手机信息权限 -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!-- 读取联系人权限，上传联系人需要用到此权限 -->
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <!-- 外存储写权限，构建语法需要用到此权限 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- 外存储读权限，构建语法需要用到此权限 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <!-- 配置权限，用来记录应用配置信息 -->
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.READ_LOGS" />
```

## 使用
初始化

在Application 的onCreate 方法中进行初始化




```
注意如果同时使用了ipsmap的导航模块,并且已经初始化导航模块,则不用初始化定位模块,ipsmap导航模块 已经对定位进行了初始化,
    IpsLocationSDK.init(new IpsLocationSDK.Configuration.Builder(this)
                .appKey(Constants.IPSMAP_APP_KEY)
                .debug(false)
                .build());
                开启debug 后有log 日志,打正式版请务必关闭debug 日志


```


定位监听,获取当前的位置,可以参考ipslocation demo ,需要提前获取定位和蓝牙权限
```

ipsClient = new IpsClient(context, map_id);
//携带用户id用法
ipsClient = new IpsClient(MainActivity.this, Constants.IPSMAP_MAP_ID,Constants.IPSMAP_USER_ID);
ipsClient.registerLocationListener(new IpsLocationListener() {
    @Override
    public void onReceiveLocation(IpsLocation ipsLocation){
    if(ipsLocation == null){
        //定位失败;
        return;
    }
    //是否在Map内
    ipsLocation.isInThisMap()

    }
});
ipsClient.start();
```

activity 结束时调用
```
@Override
protected void onDestroy() {
    super.onDestroy();
    ipsClient.stop();
}
```

## 混淆
```
-keep public class com.sails.engine.patterns.IconPatterns
```
