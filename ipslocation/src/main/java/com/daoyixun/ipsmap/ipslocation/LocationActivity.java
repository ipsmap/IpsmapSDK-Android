package com.daoyixun.ipsmap.ipslocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daoyixun.location.ipsmap.IpsClient;
import com.daoyixun.location.ipsmap.IpsLocation;
import com.daoyixun.location.ipsmap.IpsLocationListener;

public class LocationActivity extends AppCompatActivity {


    private IpsClient ipsClient;
    private EditText edTextTargetId;
    private Button btnSetTarget;
    private String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //没有携带用户id
        //ipsClient = new IpsClient(LocationActivity.this, Constants.IPSMAP_MAP_ID);
        //如果有用户id ,请用下面的构造方法
        ipsClient = new IpsClient(LocationActivity.this, Constants.IPSMAP_MAP_ID,Constants.IPSMAP_USER_ID);
        ipsClient.registerLocationListener(new IpsLocationListener() {
            @Override
            public void onReceiveLocation(IpsLocation ipsLocation) {
                if (ipsLocation == null) {
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //定位位置是否在map中
                Toast.makeText(getApplicationContext(), ipsLocation.isInThisMap() + "" + ipsLocation.getNearLocationRegion(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startLocation(View view) {
            ipsClient.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ipsClient.stop();
    }
}
