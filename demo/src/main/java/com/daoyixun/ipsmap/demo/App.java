package com.daoyixun.ipsmap.demo;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.daoyixun.ipsmap.IpsMapSDK;
import com.daoyixun.ipsmap.ShareToWechatListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class App extends Application implements ShareToWechatListener{
    @Override
    public void onCreate() {
        super.onCreate();

        IpsMapSDK.init(new IpsMapSDK.Configuration.Builder(this)
                .appKey(Constants.IPSMAP_APP_KEY)
//                .shareToWechatListener(this)
                //正式版请关闭
//                .debug(false)
                .build());
    }

    @Override
    public void shareToWechat(String url, String title, String description, Bitmap bitmap) {
        try {
            IWXAPI wxApi = WXAPIFactory.createWXAPI(this, "YOUR WECHAT APP_ID");
            wxApi.registerApp("YOUR WECHAT APP_ID");
            if (!wxApi.isWXAppInstalled()) {
                Toast.makeText(this, "未安装微信", Toast.LENGTH_SHORT).show();
                return;
            }
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = description;
            msg.setThumbImage(bitmap);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            wxApi.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
