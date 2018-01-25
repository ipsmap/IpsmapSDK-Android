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

import java.util.ArrayList;

public class NavActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private IpsClient ipsClient;
    private EditText edTextTargetId;
    private Button btnSetTarget;
    private String targetId;
    private Button btnNavTo;
    private IpsNavigation ipsNavigation;
    private TextView tvNavContent;
    private ArrayList<Object> targetIdList;

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
        targetIdList = new ArrayList<>();
        btnSetTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = edTextTargetId.getText();
                targetIdList.clear();
                if (!TextUtils.isEmpty(text.toString().trim())) {
                    targetId = text.toString().trim();
                } else {
                    targetIdList.add("Mv22bb4QWI");
                    targetIdList.add("UJx02Y1FyR");
                    targetIdList.add("bXTu1S1Dzk");
                    targetIdList.add("rIOVisqH8o");
                    targetIdList.add("481RceIJ2K");
                }
                targData = ipsNavigation.setTargetId(targetIdList);
                for (int i = 0; i < targData.size(); i++) {
                    UserToTargetData userToTargetData = targData.get(i);
                    L.e(" dddd", "userToTargetData " + i + "   " + userToTargetData.toString());
                }
            }
        });
        btnNavTo = (Button) findViewById(R.id.btn_nav_to);
        btnNavTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                targData = ipsNavigation.setTargetId(targetIdList);
                for (int i = 0; i < targData.size(); i++) {
                    UserToTargetData userToTargetData = targData.get(i);
//                    L.e(" dddd","userToTargetData "+ i + "   "+ userToTargetData.toString());
                }
//                + "当前的楼层:" + userToTargetData.getLocationFloor()

                userToTargetDataList = ipsNavigation.startRouting();
                String content = "";
                if (userToTargetDataList != null) {
                    for (int i = 0; i < userToTargetDataList.size(); i++) {
                        UserToTargetData userToTargetData = userToTargetDataList.get(i);
                        if (userToTargetDataList != null) {
                            boolean success = userToTargetData.isSuccess();
                            if (success) {
                                L.e("dddd", userToTargetData.toString());
                                String cont = i + "目的地:" + userToTargetData.getTarget() + " 距离 " + userToTargetData.getToTargetDistance() + "楼层:"
                                        + userToTargetData.getTargetFloor()  +
                                        "location "+userToTargetData.getNearLocationRegionName()+
                                        "\r\n";
                                content += cont;
                                tvNavContent.setText(content);
                            } else {
                                String cont = i + "   " + "flase " + "  " + userToTargetData.getErrorMessage() + "\r\n";
                                content += cont;
                                tvNavContent.setText(content);
                            }
                        } else {
                            L.e("dddd", userToTargetData.toString());
                        }
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
