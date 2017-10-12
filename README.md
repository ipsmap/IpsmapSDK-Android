# IpsmapSDK-Android

[![license](https://img.shields.io/hexpm/l/plug.svg)](https://raw.githubusercontent.com/typ0520/fastdex/master/LICENSE)
[![Download](https://api.bintray.com/packages/xun/maven/com.ipsmap/images/download.svg) ](https://bintray.com/xun/maven/com.ipsmap/_latestVersion)
[![API](https://img.shields.io/badge/API-18%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=18)
[![Contact](https://img.shields.io/badge/Author-IpsMap-orange.svg?style=flat)](http://ipsmap.com)

IpsmapSDK-Android 是一套基于 Android 4.3 及以上版本的室内地图应用程序开发接口，供开发者在自己的Android应用中加入室内地图相关的功能，包括：地图显示（多楼层、多栋楼）、室内导航、模拟导航、语音播报等功能。

## 获取AppKey和MapId
请联系dev@ipsmap.com

## 添加依赖

```
compile ('com.ipsmap:ipsmap:1.2.7', {
        exclude group: 'com.android.support'
    })
```
如果仅仅使用定位模块请参考ipslocation demo README

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
``` 不使用微信分享
    IpsMapSDK.init(context, IPSMAP_APP_KEY);
    或
    使用微信分享
    IpsMapSDK.init(new IpsMapSDK.Configuration.Builder(context)
                .appKey(Constants.IPSMAP_APP_KEY)
                .shareToWechatListener(this)
                .build());
                

```
SDK内部实现了分享功能，使用的前提是需要申请微信的appkey，并且需要实现接口ShareToWechatListener接口
参考代码如下：
```

    参考代码
   @Override
    public void shareToWechat(String url, String title, String description, Bitmap bitmap) {
        try {
            IWXAPI wxApi = WXAPIFactory.createWXAPI(this, "YOUR WECHAT APP_ID");
            wxApi.registerApp("YOUR WECHAT APP_ID");
            if (!wxApi.isWXAppInstalled()) {
                Toast.makeText(this, "未安装微信", Toast.LENGTH_SHORT).show();
                return;
            }
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = description;
            msg.setThumbImage(bitmap);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            wxApi.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
                

```

```
<!--微信分享-->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:host="share"
            android:scheme=你的scheme></data>
    </intent-filter>
<!--微信分享结束-->
```



启动地图
```
IpsMapSDK.openIpsMapActivity(context, map_id);
或
IpsMapSDK.openIpsMapActivity(context, map_id, target_id);
```

定位监听,获取当前的位置,可以参考ipslocation demo ,需要提前获取定位和蓝牙权限
```
ipsClient = new IpsClient(context, map_id); 
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
-dontwarn com.baidu.**
-keep class com.baidu.** {*;}
-dontwarn com.iflytek.**
-keep class com.iflytek.**{*;}
-keep public class com.sails.engine.patterns.IconPatterns
```

微信分享以及复制跳转请参考demo
