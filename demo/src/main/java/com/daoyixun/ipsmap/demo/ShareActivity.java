package com.daoyixun.ipsmap.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.daoyixun.ipsmap.IpsMapSDK;

/**
 * author:chen
 * time:2017/9/5
 * desc:
 */
public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IpsMapSDK.shareLinkToMapView(getIntent());
                finish();
            }
        }, 500);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        IpsMapSDK.shareLinkToMapView(intent);
        finish();
    }
}
