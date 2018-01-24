package com.daoyixun.ipsmap.ipslocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daoyixun.location.ipsmap.IpsClient;
import com.daoyixun.location.ipsmap.IpsNavigation;
import com.daoyixun.location.ipsmap.UserToTargetLocationListener;
import com.daoyixun.location.ipsmap.model.bean.InitNavErrorException;
import com.daoyixun.location.ipsmap.model.bean.UserToTargetData;
import com.daoyixun.location.ipsmap.utils.L;
import com.daoyixun.location.ipsmap.utils.T;

public class NavActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private IpsClient ipsClient;
    private EditText edTextTargetId;
    private Button btnSetTarget;
    private String targetId;
    private Button btnNavTo;
    private IpsNavigation ipsNavigation;
    private TextView tvNavContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        initView();
        initData();
        initNav();
    }



    private void initView() {
        edTextTargetId = (EditText) findViewById(R.id.ed_text_tagetid);
        btnSetTarget = (Button) findViewById(R.id.btn_settarget);
        tvNavContent = (TextView) findViewById(R.id.tv_content);
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
    }

    private void initData() {

    }

    private void initNav() {
        if (ipsNavigation == null){
            ipsNavigation = new IpsNavigation(getBaseContext(), "VhsehJzuZA");
            ipsNavigation.registerUserToTargetLocationListener(new UserToTargetLocationListener() {
                @Override
                public void onError(InitNavErrorException errorException) {
                    com.daoyixun.location.ipsmap.utils.L.e("ddddd","error "+errorException.toString());
                }
            });
        }
    }
}
