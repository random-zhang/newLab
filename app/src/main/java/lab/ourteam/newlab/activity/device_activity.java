package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lab.ourteam.newlab.MQTTMessage;
import lab.ourteam.newlab.service.MQTTService;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.selector.class_selector;
import lab.ourteam.newlab.pause_State;
import lab.ourteam.newlab.result_bridge;

public class device_activity extends Activity {
    private Switch on_off_switch;
    private Button sheding;
    private Button dialog_cancel;
    private Button dialog_yes;
    private Button temperature_dialog_yes;
    private Button temperature_dialog_cancel;
    private Button appoint_dialog_yes;
    private Button appoint_dialog_cancel;
    private ToggleButton pause;
    private TextView appoint_selector;
    private TextView time_selector;
    private TextView sv_selector;
    private TextView temperature;
    private TextView hint_text;
    private AlertDialog.Builder builder;
    private AlertDialog.Builder tempterature_builder;
    private AlertDialog.Builder appoint_builder;
    private AlertDialog time_dialog;
    private AlertDialog tempterature_dialog;
    private AlertDialog appoint_dialog;
    private NumberPicker appoint_hour_picker;
    private NumberPicker appoint_minute_picker;
    private NumberPicker appoint_second_picker;
    private NumberPicker hour_picker;
    private NumberPicker minute_picker;
    private NumberPicker second_picker;
    private NumberPicker integer_picker;
    private NumberPicker decimal_picker;
    private View appoint_dialog_view;
    private View time_dialog_view;
    private View tempterature_view;
    private double sv_time;
    private double appoint_time;
    private double sv;
    private double integer;
    private double decimal;
    private double cv;
    private boolean isAppoint;//选择appoint或time
    private long time_remaining;//保存从service回调的剩余时间变量
    public device_activity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);

        //Intent intent = getIntent();
        //ImageView returnMenu=(ImageView)findViewById(R.id.list_return_menu);
       /*returnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        EventBus.getDefault().register(this);
        //初始化区
        appoint_time = 0;
        sv_time = 0;
        sv = 0;
        isAppoint = true;
        initAlertDialog();
        initId();
        hour_picker.setMaxValue(23);
        hour_picker.setMinValue(0);
        minute_picker.setMaxValue(59);
        minute_picker.setMinValue(0);
        second_picker.setMaxValue(59);
        second_picker.setMinValue(0);
        decimal_picker.setMinValue(0);
        decimal_picker.setMaxValue(99);
        integer_picker.setMaxValue(100);
        integer_picker.setMinValue(0);
        appoint_hour_picker.setMaxValue(23);
        appoint_hour_picker.setMinValue(0);
        appoint_minute_picker.setMaxValue(59);
        appoint_minute_picker.setMinValue(0);
        appoint_second_picker.setMaxValue(59);
        appoint_second_picker.setMinValue(0);
        initListener();
        on_off_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("cmd", "on_off");
                } catch (JSONException e) {

                }
                String str = obj.toString();
                MQTTService.publish(str);
            }
        });
        sheding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用正则表达式提取时间
                String[] appoint_str = appoint_selector.getText().toString().split(":");
                String[] sv_time_str = time_selector.getText().toString().split(":");
                appoint_time = getValue(appoint_str);
                sv_time = getValue(sv_time_str);
                hint_text.setVisibility(View.VISIBLE);
                JSONObject sub_obj = new JSONObject();
                JSONObject obj = new JSONObject();
                try {
                    sub_obj.put("SV", sv);
                    sub_obj.put("setTime", sv_time);
                    sub_obj.put("appointment", appoint_time);
                    obj.put("work_process", sub_obj);
                } catch (JSONException e) {
                }
                String str = obj.toString();
                MQTTService.publish(str);
                // if (time!= null) {
                //  time.cancel();
                // }

                Intent i = new Intent(device_activity.this, MQTTService.class);
                // if (appoint_time != 0) {
                // isappoint = true;
                // i.putExtra("isappoint",true);
                i.putExtra("class_selector", class_selector.MainActivity);
                i.putExtra("retime",true);//是否重置画布
                i.putExtra("count_down",true);
                i.putExtra("appoint_time",appoint_time);
                if(appoint_time<=0) {
                    i.putExtra("isAppoint", false);
                }else
                    i.putExtra("isdraw", true);
                i.putExtra("sv_time",sv_time);

                i.putExtra("pause_state", pause_State.NEVER_PAUSE);
                //startCountDown((long) (appoint_time + sv_time) * 60 * 1000, 60 * 1000);
                //time.start();
                // } else {
                //   isappoint = false;
                // i.putExtra("isappoint",false);
                //  i.putExtra("count_down",true);
                //time=new TimeCount((long)sv_time*60*1000,60*1000);
                //time.start();
                // }
                startService(i);
            }
        });
        pause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //hint_text.setText("非暂停");
                    /**
                     * 想法
                     * 获取暂停前时间
                     * 暂停后按暂停时间来
                     * putExtra发送
                     * 图像暂停
                     * 发送isPause=true；
                     * 绘图处理isDraw=false;
                     * temp_time=total;保存暂停时时间
                     * 暂停过后total=temp_time//恢复时间
                     *
                     */
                    Intent i=new Intent(device_activity.this,MQTTService.class);
                    i.putExtra("class_selector",class_selector.MainActivity);
                    i.putExtra("pause_state",pause_State.ONCE_PAUSE);
                    //Toast.makeText(getApplicationContext(),"isAppoint="+isAppoint,Toast.LENGTH_SHORT).show();
                    if(isAppoint){
                        i.putExtra("isAppoint",true);
                        i.putExtra("sv_time",sv_time);
                        Publish(sv, sv_time, time_remaining/(60*1000));
                    }else{
                        i.putExtra("isdraw",true);
                        i.putExtra("isAppoint",false);
                        Publish(sv,time_remaining/(60*1000),0);
                    }
                    startService(i);
                } else {
                    hint_text.setText("暂停中...");
                    //在此发送保存的状态给service方便重新开始时读取
                    Intent i=new Intent(device_activity.this,MQTTService.class);
                    i.putExtra("class_selector",class_selector.MainActivity);
                    i.putExtra("pause_state",pause_State.IS_PAUSE);
                    i.putExtra("pause_time_remaining",time_remaining);
                    if(isAppoint)
                        i.putExtra("isAppoint",true);
                    else
                        i.putExtra("isAppoint",false);
                    startService(i);
                    Publish(cv,0,0);
                    //剩余加热或者预约时
                }
            }
        });
        //选择器相关代码
        appoint_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appoint_dialog.show();
            }
        });
        time_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_dialog.show();
            }
        });
        sv_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempterature_dialog.show();
            }
        });
        temperature_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integer = integer_picker.getValue();
                decimal = decimal_picker.getValue();
                String str = String.format("%02d.%02dºC", (int) integer, (int) decimal);
                sv_selector.setText(str);
                sv = integer + decimal / 100;
                tempterature_dialog.dismiss();
            }
        });
        temperature_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempterature_dialog.dismiss();
            }
        });
        return  ;
    }
    public void initId(){
        on_off_switch=(Switch)findViewById(R.id.on_off_switch);
        sheding=(Button)findViewById(R.id.sheding);
        appoint_selector=(TextView)findViewById(R.id.appoint_selector);
        time_selector=(TextView)findViewById(R.id.time_selector);
        sv_selector=(TextView)findViewById(R.id.sv_selector);
        temperature=(TextView)findViewById(R.id.temperature) ;
        hint_text=(TextView)findViewById(R.id.hint_text);

        temperature_dialog_yes=(Button)tempterature_view.findViewById(R.id.temperature_dialog_yes) ;
        temperature_dialog_cancel=(Button)tempterature_view.findViewById(R.id.temperature_dialog_cancel) ;
        dialog_cancel=(Button)time_dialog_view.findViewById(R.id.time_dialog_cancel);
        dialog_yes=(Button)time_dialog_view.findViewById(R.id.time_dialog_yes);
        pause=(ToggleButton)findViewById(R.id.pause) ;
        appoint_hour_picker=(NumberPicker)appoint_dialog_view.findViewById(R.id.appoint_hour);
        appoint_minute_picker=(NumberPicker)appoint_dialog_view.findViewById(R.id.appoint_minute);
        appoint_second_picker=(NumberPicker) appoint_dialog_view.findViewById(R.id.appoint_second);
        hour_picker=(NumberPicker)time_dialog_view.findViewById(R.id.hour);
        minute_picker=(NumberPicker)time_dialog_view.findViewById(R.id.minute);
        second_picker=(NumberPicker)time_dialog_view.findViewById(R.id.second);
        decimal_picker=(NumberPicker)tempterature_view.findViewById(R.id.temperature_decimal);
        integer_picker=(NumberPicker) tempterature_view.findViewById(R.id.temperature_integer);
        appoint_dialog_cancel=(Button)appoint_dialog_view.findViewById(R.id.appoint_time_dialog_cancel);
        appoint_dialog_yes=(Button)appoint_dialog_view.findViewById(R.id.appoint_time_dialog_yes);
    }
    public void initAlertDialog(){
        LayoutInflater inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(device_activity.this);
        time_dialog_view = inflater.inflate(R.layout.time_dialog, null, false);
        builder.setView(time_dialog_view);
        builder.setCancelable(true);
        time_dialog = builder.create();
        LayoutInflater temperature_inflater = getLayoutInflater();
        tempterature_builder = new AlertDialog.Builder(device_activity.this);
        tempterature_view = temperature_inflater.inflate(R.layout.temperature_dialog, null, false);
        tempterature_builder.setView(tempterature_view);
        tempterature_builder.setCancelable(true);
        tempterature_dialog = tempterature_builder.create();
        LayoutInflater appoint_inflater=getLayoutInflater();
        appoint_builder=new AlertDialog.Builder(device_activity.this);
        appoint_dialog_view=appoint_inflater.inflate(R.layout.appiont_dialog,null,false);
        appoint_builder.setView(appoint_dialog_view);
        appoint_builder.setCancelable(true);
        appoint_dialog=appoint_builder.create();
    }
    public double getValue(String[] str)
    {
        int sub_hour=Integer.parseInt(str[0]);
        int sub_minute=Integer.parseInt(str[1]);
        int sub_second=Integer.parseInt(str[2]);
        double total=sub_hour*60+sub_minute+(double)sub_second/60;
        return total;
    }
    public void Publish(double sv,double setTime,double appointment)
    {
        JSONObject sub_obj=new JSONObject();
        JSONObject obj=new JSONObject();
        try{
            sub_obj.put("SV",sv);//保温状态
            sub_obj.put("setTime",setTime);
            sub_obj.put("appointment",appointment);
            obj.put("work_process",sub_obj);
        }catch(JSONException e){
        }
        String str=obj.toString();
        MQTTService.publish(str);
    }
    private void initListener() {//监听
        View.OnClickListener button_Listener=new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                switch(arg0.getId()){
                    case R.id.appoint_time_dialog_cancel:{
                        appoint_dialog.dismiss();
                        break;
                    }
                    case R.id.appoint_time_dialog_yes:{
                        double hour = appoint_hour_picker.getValue();
                        double minute = appoint_minute_picker.getValue();
                        double second = appoint_second_picker.getValue();
                        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
                        Date time = new Date();
                        time.setHours((int) hour);
                        time.setMinutes((int) minute);
                        time.setSeconds((int) second);
                        final String str = ft.format(time);
                        //二十秒刷新一次
                        appoint_selector.setText(str);//内部类不能引用本地变量，必须声明为final
                        appoint_dialog.dismiss();
                        break;
                    }
                    case R.id.time_dialog_cancel:{
                        time_dialog.dismiss();
                        break;
                    }
                    case R.id.time_dialog_yes:{
                        double hour = hour_picker.getValue();
                        double minute = minute_picker.getValue();
                        double second = second_picker.getValue();
                        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
                        Date time = new Date();
                        time.setHours((int) hour);
                        time.setMinutes((int) minute);
                        time.setSeconds((int) second);
                        final   String str = ft.format(time);
                        time_selector.setText(str); //内部类不能引用本地变量，必须声明为final
                        time_dialog.dismiss();
                        break;
                    }
                }
            }
        };
        appoint_dialog_yes.setOnClickListener(button_Listener);
        appoint_dialog_cancel.setOnClickListener(button_Listener);
        dialog_cancel.setOnClickListener(button_Listener);
        dialog_yes.setOnClickListener(button_Listener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMqttMessage(MQTTMessage mqttMessage) throws JSONException {

        Log.i(MQTTService.TAG, "get message:" + mqttMessage.getMessage());
        //total_second = mqttMessage.getTotal();//同步时间
        //Toast.makeText(this,mqttMessage.getMessage(),Toast.LENGTH_SHORT).show();
        JSONObject obj = new JSONObject(mqttMessage.getMessage());
        if (!obj.isNull("fields")) {
            JSONObject sub_obj = obj.getJSONObject("fields");
            DecimalFormat df = new DecimalFormat("00.00");
            cv = sub_obj.getDouble("CV");
            temperature.setText(df.format(cv) + "ºC");
        }
    }
    /**
     * 接收从service中回调的剩余时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getResult(result_bridge result){
        Log.i(MQTTService.TAG,result.toString());
        isAppoint=result.getIsAppoint();
        time_remaining=result.getTime_remaining();
        boolean hint=result.getHint();
        if(hint){
            hint_text.setVisibility(View.INVISIBLE);
        }
        if(isAppoint){
            int d1=(int)time_remaining/(60*1000);//分钟
            String str;
            str= String.format("将在%d分钟后开始加热，请稍等...",d1);
            hint_text.setText(str);

            double d2=time_remaining/1000;
            int hour = (int)(d2-d2%3600)/3600;
            int minute =(int)(d2%3600-d2%3600%60)/60;
            int second = (int)d2%3600%60;
            SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
            Date time = new Date();
            time.setHours(hour);
            time.setMinutes( minute);
            time.setSeconds( second);
            String str1 = ft.format(time);
            if(d2%10==0)
                appoint_selector.setText(str1);
        }else{
            int d1=(int)time_remaining/(60*1000);
            String str;
            str= String.format("将在%d分钟后停止加热，请稍等...",d1);
            hint_text.setText(str);
            int series_sv=result.getSeries_sv();//獲取從activity傳來的更改後的sv
            boolean isSeries_sv=result.getIsSeries_sv();
            String sv_str = String.format("%02d.%02dºC", series_sv, 0);
            if(isSeries_sv)
                sv_selector.setText(sv_str);
            double d2=time_remaining/1000;
            int hour = (int)(d2-d2%3600)/3600;
            int minute =(int)(d2%3600-d2%3600%60)/60;
            int second = (int)d2%3600%60;
            SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
            Date time = new Date();
            time.setHours(hour);
            time.setMinutes( minute);
            time.setSeconds( second);
            String str1 = ft.format(time);
            if(d2%10==0)
                time_selector.setText(str1);
        }
    }

}
