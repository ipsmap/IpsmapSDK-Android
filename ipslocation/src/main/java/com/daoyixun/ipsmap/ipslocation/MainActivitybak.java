//package com.daoyixun.ipsmap.ipslocation;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.daoyixun.location.ipsmap.IpsClient;
//import com.daoyixun.location.ipsmap.IpsLocation;
//import com.daoyixun.location.ipsmap.IpsLocationListener;
//import com.daoyixun.location.ipsmap.model.bean.BackgroundData;
//import com.daoyixun.location.ipsmap.utils.L;
//import com.daoyixun.location.ipsmap.utils.T;
//import com.daoyixun.location.uploadlocation.DaemonEnv;
//import com.daoyixun.location.uploadlocation.IntentWrapper;
//import com.daoyixun.location.uploadlocation.IpsLocationBackgroundListener;
//import com.daoyixun.location.uploadlocation.impl.TraceServiceImpl;
//
//import java.util.List;
//
//public class MainActivitybak extends AppCompatActivity {
//
//    private static final int REQUEST_LOCATION = 1;
//    private IpsClient ipsClient;
//
//
//    private Button btnStartUpload;
//    private Button btnAutoStart;
//    private Button btnStartAuthority;
//    private Button btnStopUpload;
//    private Button btnAutoOepnBle;
//    private Button btnIsInHospital;
//    private boolean isAutoOpenBLE;
//    private Button btnRequestPermission;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        btnRequestPermission = (Button) findViewById(R.id.btn_request_permission);
//
//        btnStartUpload = (Button) findViewById(R.id.btn_start_upload);
//        btnAutoStart = (Button) findViewById(R.id.btn_auto_start);
//        btnStartAuthority = (Button) findViewById(R.id.btn_start_authority);
//        btnStopUpload = (Button) findViewById(R.id.btn_stop_upload);
//        btnAutoOepnBle = (Button) findViewById(R.id.btn_auto_oepn_ble);
//        btnIsInHospital = (Button) findViewById(R.id.btn_is_in_hospital);
//
//
//        btnRequestPermission.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.BLUETOOTH);
//            }
//        });
//
//
//
//        List<IntentWrapper> hasAutoStartListMatters = IntentWrapper.hasAutoStartListMatters(MainActivitybak.this, "轨迹跟踪服务");
//        if (hasAutoStartListMatters.size() >0 ){
//            btnAutoStart.setVisibility(View.VISIBLE);
//        }else {
//            btnAutoStart.setVisibility(View.GONE);
//        }
//        List<IntentWrapper> hasWhiteListMatters = IntentWrapper.hasWhiteListMatters(MainActivitybak.this, "轨迹跟踪服务");
//        if (hasWhiteListMatters.size() > 0){
//            btnStartAuthority.setVisibility(View.VISIBLE);
//        }else {
//            btnStartAuthority.setVisibility(View.GONE);
//        }
//
//
//
//
//        //手动点击后开始背景定位,需要开始定位权限和蓝牙权限
//        btnStartUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    startUploadLocationService();
//                } else {
//                    Toast.makeText(MainActivitybak.this,"沒有定位权限!",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//
//
//        //自启动,需要开启
//        btnAutoStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<IntentWrapper> hasAutoStartListMatters = IntentWrapper.hasAutoStartListMatters(MainActivitybak.this, "轨迹跟踪服务");
//                if (hasAutoStartListMatters.size() >0 ){
//                    IntentWrapper.autoStartListMatters(MainActivitybak.this, "轨迹跟踪服务");
//                }
//            }
//        });
//
//        //省电和神隐模式,不开启,在息屏状态下,几分钟后蓝牙将会停止扫描
//        btnStartAuthority.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentWrapper.whiteListMatters(MainActivitybak.this, "轨迹跟踪服务");
//            }
//        });
//
//        //请求蓝牙是否已自动请求打开蓝牙,默认自动请求打开蓝牙,最好在app 死亡的时候,关闭,
//        //在蓝牙没有开启的情况下,程序请求开启蓝牙
//        btnAutoOepnBle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                T.showShort(""+isAutoOpenBLE);
//                TraceServiceImpl.setAutoTurnOnBT(isAutoOpenBLE);
//                isAutoOpenBLE = !isAutoOpenBLE;
//            }
//        });
//
////        initLocation();
//        //判断是否在医院内
//        btnIsInHospital.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ipsClient.start();
//                if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                    //拥有了此权限
//                    ipsClient.start();
//                } else {
//                    Toast.makeText(MainActivitybak.this,"沒有定位权限!",Toast.LENGTH_LONG).show();
//                    //还没有对应权限
//                    //  requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
//                }
//            }
//        });
//
//        //重点注意###### 在不需要上传和返回信息,手动停止服务,在app退出的时候可以显示是否关闭服务,后面就会收不到会议的数据
//
//        //1.开启自启动和省电优化关闭 服务可以很长时间后台存活,会返回数据,但是注意你的接口上传oa 是否存活
//        //2.在没有开启自启动和省电优化关闭 ,服务有可能在被杀死和息屏条件下获取不到数据
//
//        btnStopUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TraceServiceImpl.stopService();
//            }
//        });
//
//
//        initIpsClent();
//
//    }
//
//    private void initIpsClent() {
//
//        ipsClient = new IpsClient(MainActivitybak.this, Constants.IPSMAP_MAP_ID);
//        ipsClient.registerLocationListener(new IpsLocationListener() {
//            @Override
//            public void onReceiveLocation(IpsLocation ipsLocation) {
//                if (ipsLocation == null) {
//                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //定位位置是否在map中
//                Toast.makeText(getApplicationContext(), ipsLocation.isInThisMap() + " "+ipsLocation.getNearLocationRegion(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void startLocation(View view) {
//        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            //拥有了此权限
//            ipsClient.start();
//        } else {
//            //还没有对应权限
//            requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    ipsClient.start();
//                } else {
//                    Toast.makeText(MainActivitybak.this, "请授予权限", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
//
//    public boolean hasPermission(String... permissons) {
//        for (String permisson : permissons) {
//            if ((ContextCompat.checkSelfPermission(this,
//                    permisson) != PackageManager.PERMISSION_GRANTED)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void requestPermission(int requestCode, String... permissions) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(permissions, requestCode);
//        }
//    }
//
//    public void startUploadLocationService() {
//        if (!DaemonEnv.sInitialized){
//            DaemonEnv.initialize(getApplicationContext(), TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
//        }else {
////            TraceServiceImpl.stopService();
//        }
//        DaemonEnv.startServiceSafely(new Intent(MainActivitybak.this, TraceServiceImpl.class));
//
//        TraceServiceImpl.registerBackgroundListerner(new IpsLocationBackgroundListener() {
//            @Override
//            public void onReceiveBackGroundLocation(BackgroundData backgroundData) {
//                //返回背景定位的数据,请保持你的服务和或者界面存活,上传数据到oa
//                //如果不存活,返回不了数据
//                L.e("dddd 2222",backgroundData.toString());
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ipsClient.stop();
//    }
//}
