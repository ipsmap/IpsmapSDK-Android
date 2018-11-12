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

compile ('com.ipsmap:ips-location:0.4.4', {
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

以下的功能都需要在在Application 的onCreate 方法中进行初始化


```
注意如果同时使用了ipsmap的导航模块,并且已经初始化导航模块,则不用初始化定位模块,ipsmap导航模块 已经对定位进行了初始化,
    IpsLocationSDK.init(new IpsLocationSDK.Configuration.Builder(this)
                .appKey(Constants.IPSMAP_APP_KEY)
                .debug(false)
                //开启debug 后有log 日志,打正式版请务必关闭debug 日志
                // 默认是false , 如果项目正式上线 debug 是false 
                //以下情况: debug 只能是 true 如果是开发人员给出的测试 mapid(在正式版道一循上不显示,道一循Bete 版的列表显示)
                .build());
```

## 功能一  定位功能
1.定位监听,获取当前的位置,可以参考ipslocation demo ,需要提前获取定位和蓝牙权限
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

2.activity 结束时调用
```
@Override
protected void onDestroy() {
    super.onDestroy();
    ipsClient.stop();
}
```




## 功能二  背景导航到目的地功能

1.初始化(sdk 进行下载地图 和初始化定位引擎 ,这里面处理蓝牙异常是否需要重启)
```
  ipsNavigation = new IpsNavigation(getBaseContext(), mapid );
            ipsNavigation.registerUserToTargetLocationListener(new UserToTargetLocationListener() {
                @Override
                public void onError(InitNavErrorException errorException) {
                    com.daoyixun.location.ipsmap.utils.L.e("ddddd","error "+errorException.toString());
                }
            });
```


2.设置 目的地 ,可以通过 构造 方法传进来 ,也 可以 通过 函数设置,注意是否成功

```
     targetIdList = new ArrayList<>();
     targetIdList.add("Mv22bb4QWI");
    targetIdList.add("UJx02Y1FyR");
    targetIdList.add("bXTu1S1Dzk");
    targetIdList.add("rIOVisqH8o");
    targetIdList.add("481RceIJ2K");
    UserToTargetData targData = ipsNavigation.setTargetId(targetIdList);
```


3.获取导航距离


```
            ArrayList<UserToTargetData> userToTargetDataList = ipsNavigation.startRouting();
             if (userToTargetDataList != null) {
                        for (int i = 0; i < userToTargetDataList.size(); i++) {
                            UserToTargetData userToTargetData = userToTargetDataList.get(i);
                            if (userToTargetDataList != null) {
                                boolean success = userToTargetData.isSuccess();
                                if (success) {
                                    L.e("dddd", userToTargetData.toString());
                                    String cont = i + "目的地:" + userToTargetData.getTarget() + " 距离 " + userToTargetData.getToTargetDistance() + "楼层:"
                                            + userToTargetData.getTargetFloor()  +
                                            "location "+userToTargetData.getNearLocationRegionName()+
                                            "\r\n";
                                    content += cont;
                                    tvNavContent.setText(content);
                                } else {
                                    String cont = i + "   " + "flase " + "  " + userToTargetData.getErrorMessage() + "\r\n";
                                    content += cont;
                                    tvNavContent.setText(content);
                                }
                            } else {
                                L.e("dddd", userToTargetData.toString());
                            }
                        }
             }

```
4.结束导航

```
ipsNavigation.stopNavigation();

```

## 混淆
```
-keep public class com.sails.engine.patterns.IconPatterns
```

错误码

```
    //Error code message
    public  static final  int ERROR_CODE_0 = 0;
    public static final String ERROR_MESSAGE_0 = "蓝牙需要重启";

    public  static final  int ERROR_CODE_1 = 1;
    public static final String ERROR_MESSAGE_1 = "没有读取地图的权限";

    public  static final  int ERROR_CODE_2 = 2;
    public static final String ERROR_MESSAGE_2 = "地图信息不完整";

    public  static final  int ERROR_CODE_3 = 3;
    public static final String ERROR_MESSAGE_3 = "网络异常 加载地图失败";

    public  static final  int ERROR_CODE_4 = 4;
    public static final String ERROR_MESSAGE_4 = "没有找到目的地,请检查id是否正确!";

    public  static final  int ERROR_CODE_5 = 5;
    public static final String ERROR_MESSAGE_5 = "路径规划失败!";

    public  static final  int ERROR_CODE_6 = 6;
    public static final String ERROR_MESSAGE_6 = "定位失败!";

    public  static final  int ERROR_CODE_7 = 7;
    public static final String ERROR_MESSAGE_7 = "正在加载地图!";

    public  static final  int ERROR_CODE_8 = 8;
    public static final String ERROR_MESSAGE_8 = "没有设置目的地列表!";
```


