# IpsmapSDK-Android

[![license](https://img.shields.io/hexpm/l/plug.svg)](https://raw.githubusercontent.com/typ0520/fastdex/master/LICENSE)
[![Download](https://api.bintray.com/packages/xun/maven/com.ipsmap/images/download.svg) ](https://bintray.com/xun/maven/com.ipsmap/_latestVersion)
[![API](https://img.shields.io/badge/API-18%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=18)
[![Contact](https://img.shields.io/badge/Author-IpsMap-orange.svg?style=flat)](http://ipsmap.com)


## 最新通知

```
请各位对接方尽快更新最新的1.5.0,请尽快强制更新
1.去除统计功能
2.优化对 9.0 的兼容性
3.优化对初始化的过程
4.性能优化
5.用户体验更佳,请尽快强制更新

```

IpsmapSDK-Android 是一套基于 Android 4.3 及以上版本的室内地图应用程序开发接口，供开发者在自己的Android应用中加入室内地图相关的功能，包括：地图显示（多楼层、多栋楼）、室内导航、模拟导航、语音播报等功能。

## 获取key
从http://ipsmap.oss-cn-shanghai.aliyuncs.com/%E9%81%93%E4%B8%80%E5%BE%AA%E5%8C%BB%E9%99%A2%E5%AE%A4%E5%86%85%E5%AF%BC%E8%88%AA%E7%B3%BB%E7%BB%9F%E5%BA%94%E7%94%A8%E6%8E%A5%E5%85%A5%E7%94%B3%E8%AF%B7%E8%A1%A8%EF%BC%8820180330%EF%BC%89.xlsx
下载表格填写后发送到  dev@ipsmap.com  ,工作人员回复

## 添加依赖

```


建议使用marven 方式，两者选其中一种，如果全部添加，编译报资源重复的错误
// marven
compile ('com.ipsmap:ipsmap:1.5.0', {
        exclude group: 'com.android.support'
    })


//或者加入aar 依赖，文件在项目的根目录下

compile project(':ips-location-release')
compile project(':ipsmap-release')


```
如果仅仅使用定位模块请参考ipslocation demo README


## 目前支持的cpu 架构 arm,暂时不支持其他架构,请配置下面的cpu架构
```
ndk {
            // 必须设置cpu类型,设置支持的 SO 库构架,强烈建议仅仅支持'armeabi',
            //如果添加全部平台的架构,包会变很大,市场面98% 都是armabi,如果想支持其他的cpu类型,请拷贝demo的跟根文件下的v7a
            //和v8a 到App相应的的cpu文件,
            //,默认仅仅支持'armeabi',不需要拷贝'armeabi',

            abiFilters 'armeabi'
}
```
## 加入权限

```
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>

```

## 使用
初始化

在Application 的onCreate 方法中进行初始化(特别注意应用有推送功能的时候,要仅仅初始化一次,第三方推送用的多服务包活机制,会启动多次 application)
``` 
  使用默认配置信息
    IpsMapSDK.init(context, IPSMAP_APP_KEY);
    或
    定制配置信息 ,使用微信分享功能请实现相关的接口
    IpsMapSDK.init(new IpsMapSDK.Configuration.Builder(context)
                .appKey(Constants.IPSMAP_APP_KEY)
                .shareToWechatListener(this)
                 .debug(false)
                  //开启debug 后有log 日志,打正式版请务必关闭debug 日志
                  // 默认是false , 如果项目正式上线 debug 是false 
                  //以下情况: debug 只能是 true 如果是开发人员给出的测试 mapid(在正式版道一循上不显示,道一循Beta 版的列表显示)
                .build());




  微信分享功能可以参考以下代码,需要替换自己申请的id

      @Override
      public void shareToWechat(String url, String title, String description, Bitmap bitmap) {
          try {
              IWXAPI wxApi = WXAPIFactory.createWXAPI(context, Constants.WECHAT_APP_ID);
              wxApi.registerApp(Constants.WECHAT_APP_ID);
              if (!wxApi.isWXAppInstalled()) {
                  T.showShort("未安装微信");
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

                
```



启动地图方式1,携带目的地和地图id,导航到目的地
```
IpsMapSDK.openIpsMapActivity(Context context, String mapId, String targetId);

```

启动地图方式2,仅仅传递地图的id
```
IpsMapSDK.openIpsMapActivity(Context context, String mapId);

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
        //是否在Map内
        //定位位置是否在map中
        String nearLocationRegion = ipsLocation.getNearLocationRegion();
        boolean isError = TextUtils.isEmpty(ipsLocation.getFloor());
        Double latitude = ipsLocation.getLatitude();
        Double longitude = ipsLocation.getLongitude();
    
        if (!isError&&!(latitude==0.0)&&!(longitude==0.0)){
    
            // 进入这里面才是确认获取到定位,定位成功
            Toast.makeText(getApplicationContext(), "isError "+!isError+ "latitude "+(!(latitude==0.0))+
                    "  longitude "+(!(latitude==0.0))+
                    "  ipsLocation"+ipsLocation.toString()+ "", Toast.LENGTH_LONG).show();
        }else {
            // 定位失败
            T.showLong("不在医院内!!");
        }
    }
});

 // 请不要频繁调用此方法,比较耗性能,出现 ANR 尝试在子线程调用
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

## FAQ
1.0
![](/pic/7991511168017_.pic.jpg)
![](/pic/8021511168507_.pic.jpg)
出现上面的类似xml资源文件缺失的情况:
两种解决方案:
1. 在通过gradle 引用是加入exclude group: 'com.android.support' ,并且自己加入compile 'com.android.support:appcompat-v7:版本号'
建议方式.建议版本号25.3.1
2. 修改项目的support 支持和  compile 'com.android.support:appcompat-v7:25.3.1' 版本号一致

2.0 
```
app如果使用了okhttp ,glide ...出现第三发开源库 冲突
两种解决方案:
1.通过  exclude group: "com.squareup.okhttp3" 方式处理
然后保留项目的okhttp和glide 
2.保持和sdk的一致引入的第三方库版本号一致.否则有可能出现冲突
```
```
"glide"             : "com.github.bumptech.glide:glide:3.7.0",
"okhttp"            : "com.squareup.okhttp3:okhttp:3.8.0",
"gson"              : "com.google.code.gson:gson:2.8.2",
 ```        


 3.0
 
![](/pic/AC0BDB3E-C313-4644-AB5F-F3C8FA209AEC.png) 
```


    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
            flatDir {
                dirs 'libs'
            }
        }
    }
    
    compileOptions {
         sourceCompatibility JavaVersion.VERSION_1_8
         targetCompatibility JavaVersion.VERSION_1_8
     }
```

4.0 
java.lang.NoClassDefFoundError: Failed resolution of: Lorg/apache/http/impl/client/DefaultHttpClient

华为MATE 10 PRO机型安装APP后闪退，其他机型暂无发现，出现上面的错误信息，是引用的第三方SDK出现的，解决方法：在manifest.xml文件中的application节点下添加

<uses-library android:name="org.apache.http.legacy" android:required="false"/>
————————————————
版权声明：本文为CSDN博主「mxy19891106」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/mxy19891106/article/details/89638064

