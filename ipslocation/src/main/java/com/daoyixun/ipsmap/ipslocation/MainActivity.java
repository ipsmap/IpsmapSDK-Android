package com.daoyixun.ipsmap.ipslocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private Button btnStartLcoation;
    private Button btnStartNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //拥有了此权限
            btnStartNav.setVisibility(View.VISIBLE);
            btnStartLcoation.setVisibility(View.VISIBLE);
        } else {
            //还没有对应权限
            requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private void initView() {
        btnStartLcoation = (Button) findViewById(R.id.btn_start_location);
        btnStartNav = (Button) findViewById(R.id.btn_start_nav);
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

    public void startLocation(View view) {
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //拥有了此权限
            Intent intent = new Intent();
            intent.setClass(getBaseContext(),LocationActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(getBaseContext(),"请授予 权限！",Toast.LENGTH_SHORT).show();
        }

    }

    public void   startNav(View view){
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(),NavActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(getBaseContext(),"请授予 权限！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnStartNav.setVisibility(View.VISIBLE);
                    btnStartLcoation.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getBaseContext(), "请授予权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
