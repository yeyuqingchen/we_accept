package com.yc.we_accept;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.List;


public class MainActivity extends Activity {

    MainActivity activity;
    private My3dMapView mapView;
    private AMap aMap;

    ProgressBar refresh;
    ImageView see;

    EMMessageListener msgListener;

    private double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        see=(ImageView) findViewById(R.id.see);
        refresh= (ProgressBar) findViewById(R.id.refresh);
        mapView = (My3dMapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        login();

    }
    //环信登陆
    private void login(){
        //登录
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(MyApplication.my153, "123456", new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        Log.i("yc","登陆成功 ");
                        setHuanXin();

                        setListener();
//                        Toast.makeText(MyApplication.getApp(),"登陆成功",Toast.LENGTH_LONG).show();
                        //--
//                        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
//
//                        String action="action1";//action可以自定义
//                        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
//                        String toUsername = my187;//发送给某个人
//                        cmdMsg.setReceipt(toUsername);
//                        cmdMsg.addBody(cmdBody);
//                        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
                        //--
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String error) {
                        Log.i("yc"," 登陆失败");
                        login();
//                        Toast.makeText(MyApplication.getApp(),"登陆失败",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();

    }
    private EMLocationMessageBody locBody;
    private void setHuanXin() {

        msgListener  = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                Log.i("yc","收到消息");
//                Toast.makeText(activity,"收到消息",Toast.LENGTH_LONG).show();
                //收到消息
//                EMMessage message=messages.get(0);
//                locBody= (EMLocationMessageBody) message.getBody();
//                lat=locBody.getLatitude();
//                lng=locBody.getLongitude();
//                init();
            }
            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                Log.i("yc","收到穿透");
                EMMessage message= messages.get(0);
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
                String[] split=action.split(":");
                if (split.length!=2)return;
                lat=Double.valueOf(split[0]);
                lng=Double.valueOf(split[1]);
                refresh.setVisibility(View.GONE);
                init();
//                Toast.makeText(activity,"收到穿透",Toast.LENGTH_LONG).show();
//                MainActivity.this.startService(new Intent("com.yc.service.SendLocationService"));
                //收到透传消息
                //收到透传消息
            }
            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }
            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }
            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }

    private void setListener() {
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("yc","点击了");
                refresh.setVisibility(View.VISIBLE);
                send();
            }
        });
    }

    //
    private void send(){

        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        String action="action1";//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        String toUsername = MyApplication.my187;//发送给某个人
        cmdMsg.setReceipt(toUsername);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("yc","onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.i("yc","onError:"+s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));    //设置中心点和缩放比例
        LatLng latto=new LatLng(lat,lng);//得到当前位置的经纬度
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latto));
        aMap.addMarker(new MarkerOptions()
                .perspective(true)
                .position(latto)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hear)));



    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}