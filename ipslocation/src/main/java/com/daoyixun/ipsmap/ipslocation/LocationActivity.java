package com.daoyixun.ipsmap.ipslocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daoyixun.location.ipsmap.IpsClient;
import com.daoyixun.location.ipsmap.IpsLocation;
import com.daoyixun.location.ipsmap.IpsLocationListener;
import com.daoyixun.location.ipsmap.utils.T;

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
                }            }
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
