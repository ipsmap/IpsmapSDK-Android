package com.daoyixun.ipsmap.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.daoyixun.ipsmap.IpsClient;
import com.daoyixun.ipsmap.IpsLocation;
import com.daoyixun.ipsmap.IpsLocationListener;
import com.daoyixun.ipsmap.IpsMapSDK;
import com.daoyixun.ipsmap.ui.activity.IpsMapActivity;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private IpsClient ipsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipsClient = new IpsClient(MainActivity.this, Constants.IPSMAP_MAP_ID);
        ipsClient.registerLocationListener(new IpsLocationListener() {
            @Override
            public void onReceiveLocation(IpsLocation ipsLocation) {
                if(ipsLocation == null){
                    Toast.makeText(getApplicationContext(),"定位失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                //定位位置是否在map中
                Toast.makeText(getApplicationContext(),ipsLocation.isInThisBuilding()+"",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openIpsMap(View view){
        IpsMapSDK.openIpsMapActivity(this, Constants.IPSMAP_MAP_ID);
    }

    public void startLocation(View view){
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //拥有了此权限
            ipsClient.start();
        } else {
            //还没有对应权限
            requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
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
