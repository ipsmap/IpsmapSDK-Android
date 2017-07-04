package com.daoyixun.ipsmap.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.daoyixun.ipsmap.IpsClient;
import com.daoyixun.ipsmap.IpsLocation;
import com.daoyixun.ipsmap.IpsLocationListener;
import com.daoyixun.ipsmap.IpsMapActivity;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

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
        OkHttpClient okHttpClient = new OkHttpClient();

    }

    public void openIpsMap(View view){
        Intent intent = new Intent(MainActivity.this, IpsMapActivity.class);
        intent.putExtra("map_id", Constants.IPSMAP_MAP_ID);
        startActivity(intent);
    }

    public void startLocation(View view){
        ipsClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ipsClient.stop();
    }
}
