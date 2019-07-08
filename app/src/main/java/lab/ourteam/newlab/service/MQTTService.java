package lab.ourteam.newlab.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.MQTTMessage;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.Utils.postToTomcat;
import lab.ourteam.newlab.pause_State;
import lab.ourteam.newlab.pointValueMessage;
import lab.ourteam.newlab.result_bridge;
import lab.ourteam.newlab.selector.class_selector;
import lecho.lib.hellocharts.model.PointValue;
import okhttp3.FormBody;

public class MQTTService extends Service {//mqtt传输服务
    public static final String TAG=MQTTService.class.getSimpleName();
    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;
    private String host="tcp://47.100.47.134:1883";
    private  String userName="admin";
    private String passWord="public";
    private static String[] subTopic={"$dp","slave_computer"};//订阅的主题
    private static String pubTopic="master_computer";//发送的对象
    private static String clientId="android";
    private long notification_time=0;
    private pause_State pause_state=pause_State.NEVER_PAUSE;
    private class_selector class_picker=class_selector.MainActivity;//默认
    private Notification.Builder notification_builder;//管理通知栏
        @Override
    public void onCreate(){
        super.onCreate();
        frontService();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        init();
        return super.onStartCommand(intent,flags,startId);
    }
    public static void publish(int subId,String msg){//发送数据
        String topic=pubTopic+subId;
        Integer qos=0;
        Boolean retained=false;
        try{
            client.publish(topic,msg.getBytes(),qos.intValue(),retained.booleanValue());
       }catch(MqttException e){
        }
    }
    private void init() {
        client=new MqttAndroidClient(this,host,clientId);
    //设置监听并回调消息
        client.setCallback(mqttCallback);
        conOpt=new MqttConnectOptions();
        //清除缓存
        conOpt.setCleanSession(true);
        conOpt.setConnectionTimeout(10);
        conOpt.setKeepAliveInterval(20);
        conOpt.setUserName(userName);
        conOpt.setPassword(passWord.toCharArray());
        //最后遗嘱消息
        boolean doConnect=true;
        String message="{\"terminal_uid\":\""+clientId+"\"}";
        String topic=pubTopic;
        Integer qos=0;
        Boolean retained=false;
        if(!message.equals("")||(!topic.equals(""))){
            //最后的遗嘱
            try{
                //如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
                conOpt.setWill(topic,message.getBytes(),qos.intValue(),retained.booleanValue());
            }catch(Exception e){
                Log.i(TAG,"Exception Occured",e);
                doConnect=false;
                iMqttActionListener.onFailure(null,e);
            }
        }
        if(doConnect){
            doClientConnection();
        }
    }
    @Override
    public void onDestroy(){
        try{client.disconnect();}catch(MqttException e){
        }
        super.onDestroy();
       // dbManager.closeDB();
        Toast.makeText(getApplicationContext(),"服务器断开连接",Toast.LENGTH_SHORT).show();
    }
// 连接服务器
    private boolean doClientConnection(){
        if(!client.isConnected()&&isConnectIsNomarl()){
           try{
              client.connect(conOpt,null,iMqttActionListener);
           }catch(MqttException e) {
               return false;
           }
           return true;
        } else{
            return true;    //连接成功
        }

    }
    //服务器是否连接成功
    private IMqttActionListener iMqttActionListener=new IMqttActionListener(){
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.i(TAG,"连接成功");
            //Toast.makeText(getApplicationContext(),"服务器连接成功",Toast.LENGTH_SHORT).show();
            try{//订阅消息
                for(String publisher:subTopic)
                   client.subscribe(publisher,1);
            }catch(MqttException e){

            }
        }
        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            //失败重连
            while (true){
                try {
                    Thread.sleep(5000);//每隔五秒重连一次
                     if(doClientConnection()) return; //如果连接成功则退出循环
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }

        }
    };
    // MQTT监听并且接受消息
    private MqttCallback mqttCallback=new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
             doClientConnection();
        }
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String str1 = new String(message.getPayload());
            JSONObject obj = new JSONObject(str1);
            if(topic.equals("slave_computer")){
                final int state=obj.getInt("state");
                new Thread(){
                    @Override
                    public void run() {
                        FormBody.Builder builder=new FormBody.Builder();
                        builder.add("subId",10001+"");
                        builder.add("status",state+"");
                        try {
                            postToTomcat.postFormData("deviceController/setBathStatus.json",builder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }


            //MQTTMessage msg = new MQTTMessage();
           // msg.setMessage(str1);
            //EventBus.getDefault().post(msg);
           // Log.i(TAG, "从mqtt服务器接受的数据:" + msg);

            if (!obj.isNull("fields")) {
                JSONObject sub_obj = obj.getJSONObject("fields");
                final  double cv = sub_obj.getDouble("CV");
                final int subId=sub_obj.getInt("subId");
                //上传到数据库
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        FormBody.Builder builder=new FormBody.Builder();
                        builder.add("subId",subId+"");
                        builder.add("cv",cv+"");
                        try {
                            postToTomcat.postFormData("deviceController/updateCv.json",builder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };
    //判断网络是否连接
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        if(info!=null||info.isAvailable()){
            String name =info.getTypeName();
            Log.i(TAG,"MQTT当前网络名称:"+name);
            return true;
        }else{
            Log.i(TAG,"MQTT没有可用网络");
            return false;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    /**
     * 前台服务
     */
    private void frontService() {
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(this.getPackageName(), this.getPackageName() + "." +"MainActivity" ));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        String notification_time_string=String.format("%d分钟",notification_time/60);
        if(Integer.parseInt(Build.VERSION.SDK) >= 26) {
            String str=String.format("%d sdkversion",Build.VERSION.SDK_INT);
            Log.i(TAG,str);
            //当sdk版本大于26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
//                     channel.enableVibration(true);//
            manager.createNotificationChannel(channel);
            notification_builder=new Notification.Builder(getApplicationContext(), id);
            notification_builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("剩余加热时间")
                    .setContentText("点击打开 新实验室")
                    .setContentIntent(pendingIntent)//延后的intent
                    .setAutoCancel(true);
            Notification notification = notification_builder.build();
            manager.notify(1, notification);
            startForeground(1,notification);
        }
        else
        {
            //当sdk版本小于26
            notification_builder=new Notification.Builder(getApplicationContext())
                    .setContentTitle("剩余加热时间")
                    .setContentText("点击打开 新实验室")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = notification_builder.build();
            manager.notify(1,notification);
            startForeground(1,notification);
        }
    }
}
