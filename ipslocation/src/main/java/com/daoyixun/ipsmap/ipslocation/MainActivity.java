package com.daoyixun.ipsmap.ipslocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daoyixun.location.ipsmap.IpsClient;
import com.daoyixun.location.ipsmap.IpsLocation;
import com.daoyixun.location.ipsmap.IpsLocationListener;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private IpsClient ipsClient;
    private EditText edTextTargetId;
    private Button btnSetTarget;
    private String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edTextTargetId = (EditText) findViewById(R.id.ed_text_tagetid);
        btnSetTarget = (Button) findViewById(R.id.btn_settarget);
        btnSetTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = edTextTargetId.getText();
                if (!TextUtils.isEmpty(text.toString().trim())){
                    targetId = text.toString().trim();
                }else {
                    targetId = "Mv22bb4QWI";
                }
                UserToTargetData targData = ipsNavigation.setTargetId(targetId);
                if (!targData.isSuccess()){
//                    Toast.sh("设置目的地失败",Toast.);
                    return;
                }
            }
        });
        btnNavTo = (Button) findViewById(R.id.btn_nav_to);
        btnNavTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserToTargetData targData = ipsNavigation.setTargetId(targetId);
                if (!targData.isSuccess()){
                    T.showShort("设置目的地失败");
                    return;
                }
                UserToTargetData userToTargetData = ipsNavigation.startRouting();
                if(userToTargetData != null){
                    boolean success = userToTargetData.isSuccess();
                    if (success){
                        L.e("dddd",userToTargetData.toString());
                        tvNavContent.setText(""+userToTargetData.getTarget() + "  "+ userToTargetData.getToTargetDistance());
                    }else {
                        tvNavContent.setText("flase "+ "  "+ userToTargetData.getErrorMessage());
                    }
                }

            }
        });

        //没有携带用户id
        //ipsClient = new IpsClient(MainActivity.this, Constants.IPSMAP_MAP_ID);
        //如果有用户id ,请用下面的构造方法
        ipsClient = new IpsClient(MainActivity.this, Constants.IPSMAP_MAP_ID,Constants.IPSMAP_USER_ID);
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


    private void initNav() {
        if (ipsNavigation == null){
            ipsNavigation = new IpsNavigation(TestActivity.this, "VhsehJzuZA", "Mv22bb4QWI");
            ipsNavigation.registerUserToTargetLocationListener(new UserToTargetLocationListener() {
                @Override
                public void onError(InitNavErrorException errorException) {
                    com.daoyixun.location.ipsmap.utils.L.e("ddddd","error "+errorException.toString());
                }
            });
        }
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
