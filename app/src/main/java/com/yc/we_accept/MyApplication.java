package com.yc.we_accept;

import android.app.Application;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
public class MyApplication extends Application {

    protected static MyApplication mInstance;
    public static LocationManager lMngr;
    public static LocationListener lLsnr;

    public static final String my153="15375113906";
    public static final String my187="18715028079";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        EMClient.getInstance().init(mInstance, new EMOptions());



    }



    public static MyApplication getApp() {
        if (mInstance != null && mInstance instanceof MyApplication) {
            return mInstance;
        } else {
            mInstance = new MyApplication();
            mInstance.onCreate();
            return mInstance;
        }
    }
}
