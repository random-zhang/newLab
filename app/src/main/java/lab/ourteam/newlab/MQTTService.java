package lab.ourteam.newlab;

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

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

public class MQTTService extends Service {
    public static final String TAG=MQTTService.class.getSimpleName();
    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;
    private String host="tcp://47.100.47.134:1883";
    private  String userName="admin";
    private String passWord="public";
    private static String subTopic="$dp";
    private static String pubTopic="master_computer";
    private static String clientId="android";
    private List<PointValue> pointvaluelist=null;
    private int k=10;
    private int j=0;
    private long total=0;
    private  int series_sv;//保存從series_Activity傳回來的設定溫度
    private boolean isAppoint=true;
    private boolean isSeries_sv=false;
    private boolean isdraw;
    private List<PointValue> pointValues;//初始化为空
    private EventBus eventBus;
    private int first_start_time;
    private  double appoint_time;
    private double sv_time;
    private myCount mc;
    private long pause_time_remaining=0;
    private long pause_total=0;
    private long notification_time=0;
    private pause_State pause_state=pause_State.NEVER_PAUSE;
    private class_selector class_picker=class_selector.MainActivity;//默认
    private Notification.Builder notification_builder;//管理通知栏
    //public DBManager dbManager;
    //private boolean isPause=false;
    @Override
    public void onCreate(){
        super.onCreate();
        pointvaluelist=new ArrayList<PointValue>();
        frontService();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        init();
       // dbManager=new DBManager(this);
       // EventBus.getDefault().register(this);
        class_picker=(class_selector)intent.getSerializableExtra("class_selector");
        isdraw=intent.getBooleanExtra("isdraw",false);
        if(class_picker==class_selector.MainActivity) {
            boolean count_down = intent.getBooleanExtra("count_down", false);
            boolean isretime = intent.getBooleanExtra("retime", false);//重新计时
            pause_state = (pause_State) intent.getSerializableExtra("pause_state");
            isAppoint = intent.getBooleanExtra("isAppoint", true);
            long ispause_time_remaining = intent.getLongExtra("pause_time_remaining", 0);
            appoint_time = intent.getDoubleExtra("appoint_time", 0);
            sv_time = intent.getDoubleExtra("sv_time", 0);

        if(pause_state==pause_State.IS_PAUSE){
            pause_time_remaining=ispause_time_remaining;
            pause_total=total;
            if(mc!=null){
                mc.cancel();
            }
            Toast.makeText(getApplicationContext(),"暂停时，剩余时间为"+pause_time_remaining/(1000)+"秒",Toast.LENGTH_SHORT).show();
        }else if(pause_state==pause_State.ONCE_PAUSE){

            if(isAppoint){
                if(mc!=null) mc.cancel();
                mc=new myCount(pause_time_remaining+(long)sv_time*60*1000,1000);
                mc.start();
            }else{
                total=pause_total;
                if(mc!=null) mc.cancel();
                mc=new myCount(pause_time_remaining,1000);
                mc.start();
            }
        }else {//非暂停状态
            Toast.makeText(getApplicationContext(),"正常状态",Toast.LENGTH_SHORT).show();
            if (isretime) {
                total = 0;//计时器归零
                pointvaluelist.clear();
            }
            if (count_down) {
                initTimer();
            }
        }
        }else if(class_picker==class_selector.series_Activity){//当series_Activity传值进来时的操作
            int series_time=intent.getIntExtra("series_time",0);//单位分钟
            series_sv=intent.getIntExtra("series_sv",0);
            isSeries_sv=intent.getBooleanExtra("isSeries_sv",false);
            Toast.makeText(getApplicationContext(),"time:"+series_time+"sv:"+series_sv,Toast.LENGTH_SHORT).show();
            if(mc!=null) mc.cancel();
            mc=new myCount(series_time*60*1000,1000);
            mc.start();
        }
        if(isdraw) {
            /**
             * 用于保存上一次点坐标，加快渲染速度
             */
            pointValueMessage message=new pointValueMessage();
            message.setIsDraw(isdraw);
            message.setPointValueList(pointvaluelist);
            message.setJAndK(j,k);
            EventBus.getDefault().post(message);
        }
        return super.onStartCommand(intent,flags,startId);
    }
    public static void publish(String msg){
        String topic=pubTopic;
        Integer qos=0;
        Boolean retained=false;
        try{
            client.publish(topic,msg.getBytes(),qos.intValue(),retained.booleanValue());
       }catch(MqttException e){

        }
    }
    private void init() {

        String uri=host;
    client=new MqttAndroidClient(this,uri,clientId);
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
    private void doClientConnection(){
        if(!client.isConnected()&&isConnectIsNomarl()){
           try{
                client.connect(conOpt,null,iMqttActionListener);
           }catch(MqttException e) {

           }
        }
    }
    //服务器是否连接成功
    private IMqttActionListener iMqttActionListener=new IMqttActionListener(){
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.i(TAG,"连接成功");
            //Toast.makeText(getApplicationContext(),"服务器连接成功",Toast.LENGTH_SHORT).show();
            try{//订阅消息
                client.subscribe(subTopic,1);
            }catch(MqttException e){

            }
        }
        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            //失败重连
           doClientConnection();
        }
    };
    // MQTT监听并且接受消息
    private MqttCallback mqttCallback=new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
             doClientConnection();
            Toast.makeText(getApplicationContext(),"服务器重连成功",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String str1 = new String(message.getPayload());
            total+=5;//充当定时器,单位秒
            MQTTMessage msg = new MQTTMessage();
           // msg.setTotal(total);
            msg.setMessage(str1);
            EventBus.getDefault().post(msg);
            Log.i(TAG, "messageArrived:" + msg);
            JSONObject obj = new JSONObject(str1);
            if (!obj.isNull("fields")) {
                JSONObject sub_obj = obj.getJSONObject("fields");
              //  DecimalFormat df = new DecimalFormat("00.00");
                double cv = sub_obj.getDouble("CV");
               // int cur_time = sub_obj.getInt("timestamp");
             //   first_start_time = sub_obj.getInt("start_time");
             //   int time = (cur_time - first_start_time);//每分钟打一个点
               if ((total%20)==0&&isdraw) {//20秒刷新一次
                   // time=time/60;
                   int total_minute = (int) total / 60;
                   pointValueMessage pointvalueMessage = new pointValueMessage();
                   if (total_minute > 9 && (total % 60) == 0) {
                       j += 1;
                       k += 1;
                   }
                   pointvalueMessage.setJAndK(j, k);
                   pointvaluelist.add(new PointValue(total_minute, (float) cv));
                   pointvalueMessage.setPointValueList(pointvaluelist);
                   EventBus.getDefault().post(pointvalueMessage);
               }
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

     /* Notification.Builder builder=new Notification.Builder(this.getApplicationContext(),);
      Intent nfIntent=new Intent(this,MainActivity.class);
      builder.setContentIntent(PendingIntent.getActivity(this,0,nfIntent,0))
              .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.icon))

      .setSmallIcon(R.mipmap.ic_launcher)

      .setWhen(System.currentTimeMillis());//设置该通知发生时间
      Notification notification=builder.build();
     */

    }
public void initTimer(){
      //  new Thread(new Runnable() {
          //  @Override
           // public void run() {
                //int second_time=0;//计时作用,单位秒
              //  Toast.makeText(getApplicationContext(),"创建新线程",Toast.LENGTH_SHORT).show();
    if(mc!=null) mc.cancel();
               mc=new myCount((int)(appoint_time+sv_time)*60*1000,1000);
               mc.start();
      //      }
      //  }).start();
}
//公共计时器
class myCount extends CountDownTimer {
private long millisinfuture;//总时间
    public myCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.millisinfuture=millisInFuture;
        //Looper.prepare();
       // Toast.makeText(getApplicationContext(),millisInFuture+"传入的秒数",Toast.LENGTH_SHORT).show();
        //Looper.loop();
    }
    @Override
    public void onTick(long millisUntilFinished){//剩余时间
/**
 * 待解决问题
 * 暂停后重启时间计算问题 */
/**
 *series_Activity传值
 */
long appoint_Time=0;
if(class_picker==class_selector.MainActivity) {
            appoint_Time = (long) appoint_time * 60 * 1000;
            if (pause_state == pause_State.ONCE_PAUSE) {
                appoint_Time = pause_time_remaining;
            }
        } else if(class_picker==class_selector.series_Activity) {
           isAppoint=false;
        }
       if(isAppoint&&(millisinfuture-millisUntilFinished)<appoint_Time){//如果有预约时间,且此时还处于预约状态(经过的时间小于预约总时间
           long time_remaining=millisUntilFinished-(long)(sv_time*60*1000);//剩余预约时间
           //Toast.makeText(getApplicationContext(),time_remaining+"",Toast.LENGTH_SHORT).show();
           result_bridge msg=new result_bridge();
           msg.setAppoint(true);
           msg.setHint(false);
           msg.setSeries_sv(0);
           msg.setTime_remaining(time_remaining);
           EventBus.getDefault().post(msg);
       }else {
           long time_remaining=millisUntilFinished;//剩余加热时间
           result_bridge msg=new result_bridge();
           msg.setAppoint(false);
           msg.setHint(false);
           msg.setisSeries_sv(isSeries_sv);
           msg.setSeries_sv(series_sv);
           msg.setTime_remaining(time_remaining);
           EventBus.getDefault().post(msg);
           notification_time=time_remaining/1000;//秒級別
          frontService();
        }
    }
    @Override
    public void onFinish(){
        //隐藏Hint
        result_bridge msg=new result_bridge();
        msg.setHint(true);
        msg.setAppoint(true);
        EventBus.getDefault().post(msg);
    }
}
}
