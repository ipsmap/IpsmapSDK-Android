package com.daoyixun.ipsmap.demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.daoyixun.ipsmap.IpsMapSDK;
import com.daoyixun.location.ipsmap.IpsClient;
import com.daoyixun.location.ipsmap.IpsLocation;
import com.daoyixun.location.ipsmap.IpsLocationListener;
import com.daoyixun.location.ipsmap.utils.IpsConstants;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE.path;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private IpsClient ipsClient;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        ipsClient = new IpsClient(MainActivity.this, Constants.IPSMAP_MAP_ID);
        ipsClient.registerLocationListener(new IpsLocationListener() {
            @Override
            public void onReceiveLocation(IpsLocation ipsLocation) {
                if (ipsLocation == null) {
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //定位位置是否在map中
                String nearLocationRegion = ipsLocation.getNearLocationRegion();
                Toast.makeText(getApplicationContext(), ipsLocation.isInThisMap() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openIpsMap(View view) {
        IpsMapSDK.openIpsMapActivity(this, Constants.IPSMAP_MAP_ID);
    }

    public void startLocation(View view) {
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //拥有了此权限
            ipsClient.start();
        } else {
            //还没有对应权限
            requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }


    public  void openSmartWeixin(View view){

        // 1.需要打正式版
        // 2.确保id 和包名正确
        // 3.demo 跑不起来是包名不正确没有申请微信
        String appId = "wxfb94585e6d4df00c"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_5c53d7d8e521"; // 填小程序原始id
        req.path = "pages/index?id="+Constants.IPSMAP_MAP_ID;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ipsClient.start();
                } else {
                    Toast.makeText(MainActivity.this, "请授予权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean hasPermission(String... permissons) {
        for (String permisson : permissons) {
            if ((ContextCompat.checkSelfPermission(this,
                    permisson) != PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    public void requestPermission(int requestCode, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ipsClient.stop();
    }

}
