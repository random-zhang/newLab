package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lab.ourteam.newlab.Bean.Bath;
import lab.ourteam.newlab.Bean.Device;
import lab.ourteam.newlab.DragFloatActionButton;
import lab.ourteam.newlab.MQTTMessage;
import lab.ourteam.newlab.Utils.postToTomcat;
import lab.ourteam.newlab.service.MQTTService;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.selector.class_selector;
import lab.ourteam.newlab.pause_State;
import lab.ourteam.newlab.result_bridge;
import okhttp3.FormBody;
import okhttp3.Response;

public class bain_marle_activity extends Activity  {//水浴锅
    private Switch on_off_switch;
    private Button sheding;
    private Button dialog_cancel;
    private Button dialog_yes;
    private Button temperature_dialog_yes;
    private Button temperature_dialog_cancel;
    private ToggleButton pause;
    private TextView time_selector;
    private TextView sv_selector;//设定温度
    private TextView hint_text;
    private TextView temperature;//当前温度
    private AlertDialog.Builder builder;
    private AlertDialog.Builder tempterature_builder;
    private AlertDialog time_dialog;
    private AlertDialog tempterature_dialog;
    private NumberPicker hour_picker;
    private NumberPicker minute_picker;
    private NumberPicker second_picker;
    private NumberPicker integer_picker;
    private NumberPicker decimal_picker;
    private View time_dialog_view;
    private View tempterature_view;
    private double integer;
    private double decimal;
    private Bath bath;
    private TextView subIdTv;//设备编号
    private  TimeAsynctask timeAsynctask;
    private ImageView bath_activity_sublist;//列表选择
    private ImageView back;
    private DragFloatActionButton dragFloatActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);
        //初始化区
        Device  device=(Device) getIntent().getSerializableExtra("device");
        /*
          下面是临时数据
         */
        bath =new Bath();
        bath.setDeviceid(device.getDeviceid());
        bath.setSuid(device.getDeviceid());
        bath.setStatus(device.getStatus());
        bath.setDevicename(device.getDevicename());
         /*
           从数据库中读取信息



         */
        /*
        new Thread(){
            @Override
            public void run() {
                FormBody.Builder builder=new FormBody.Builder();
                builder.add("subId",10001+"");
                try {
                    String status=postToTomcat.postFormData("deviceController/getBathStatus.json",builder);
                    bath.setStatus(Integer.parseInt(status));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
        bath.setSv(0.0);
        bath.setCv(0.0);
        bath.setSt(0.0);
        bath.setCt(0.0);
        initAlertDialog();
        initId();
        initUI();
        initListener();
    }

    public void initId() {
        on_off_switch = (Switch) findViewById(R.id.on_off_switch);
        sheding = (Button) findViewById(R.id.sheding);
        time_selector = (TextView) findViewById(R.id.time_selector);
        sv_selector = (TextView) findViewById(R.id.sv_selector);
        temperature = (TextView) findViewById(R.id.temperature);
        hint_text = (TextView) findViewById(R.id.hint_text);
        temperature_dialog_yes = (Button) tempterature_view.findViewById(R.id.temperature_dialog_yes);
        temperature_dialog_cancel = (Button) tempterature_view.findViewById(R.id.temperature_dialog_cancel);
        dialog_cancel = (Button) time_dialog_view.findViewById(R.id.time_dialog_cancel);
        dialog_yes = (Button) time_dialog_view.findViewById(R.id.time_dialog_yes);
        pause = (ToggleButton) findViewById(R.id.pause);
        hour_picker = (NumberPicker) time_dialog_view.findViewById(R.id.hour);
        minute_picker = (NumberPicker) time_dialog_view.findViewById(R.id.minute);
        second_picker = (NumberPicker) time_dialog_view.findViewById(R.id.second);
        decimal_picker = (NumberPicker) tempterature_view.findViewById(R.id.temperature_decimal);
        integer_picker = (NumberPicker) tempterature_view.findViewById(R.id.temperature_integer);
        subIdTv = findViewById(R.id.bath_activity_subId);
        bath_activity_sublist=findViewById(R.id.bath_activity_sublist);
        dragFloatActionButton= (DragFloatActionButton) findViewById(R.id.bathActivityFloatBtn);
        back=findViewById(R.id.bath_activity_return_menu);
    }
    public void initAlertDialog() {//初始化对话框
        LayoutInflater inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(bain_marle_activity.this);
        time_dialog_view = inflater.inflate(R.layout.time_dialog, null, false);
        builder.setView(time_dialog_view);
        builder.setCancelable(true);
        time_dialog = builder.create();
        tempterature_builder = new AlertDialog.Builder(bain_marle_activity.this);
        tempterature_view = inflater.inflate(R.layout.temperature_dialog, null, false);
        tempterature_builder.setView(tempterature_view);
        tempterature_builder.setCancelable(true);
        tempterature_dialog = tempterature_builder.create();
        /*appoint_builder = new AlertDialog.Builder(bain_marle_activity.this);
        appoint_dialog_view = inflater.inflate(R.layout.appiont_dialog, null, false);
        appoint_builder.setView(appoint_dialog_view);
        appoint_builder.setCancelable(true);
        appoint_dialog = appoint_builder.create();*/
    }
    public double getValue(String[] str)//由字符串转化为分钟
    {
        int sub_hour = Integer.parseInt(str[0]);
        int sub_minute = Integer.parseInt(str[1]);
        int sub_second = Integer.parseInt(str[2]);
        double total = sub_hour * 60 + sub_minute + (double) sub_second / 60;
        return total;
    }
    public void Publish(double sv, double setTime, double appointment) {
        JSONObject sub_obj = new JSONObject();
        JSONObject obj = new JSONObject();
        try {
            sub_obj.put("SV", sv);//保温状态
            sub_obj.put("setTime", setTime);
            sub_obj.put("appointment", appointment);
            obj.put("work_process", sub_obj);
        } catch (JSONException e) {
        }
        String str = obj.toString();
        MQTTService.publish(bath.getSubId(), str);
    }

    public void initUI() {//初始化UI
        subIdTv.setText(bath.getSuid() + "");
        if (bath.getStatus()==0)
            on_off_switch.setChecked(false);
        else
            on_off_switch.setChecked(true);
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
    }

    private void initListener() {//监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View.OnClickListener button_Listener = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                switch (arg0.getId()) {
                    case R.id.time_dialog_cancel: {//设定时间的对话框
                        time_dialog.dismiss();
                        break;
                    }
                    case R.id.time_dialog_yes: {
                        double hour = hour_picker.getValue();
                        double minute = minute_picker.getValue();
                        double second = second_picker.getValue();
                        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
                        Date time = new Date();
                        time.setHours((int) hour);
                        time.setMinutes((int) minute);
                        time.setSeconds((int) second);
                        final String str = ft.format(time);
                        time_selector.setText(str); //内部类不能引用本地变量，必须声明为final
                        time_dialog.dismiss();
                        break;
                    }
                    case R.id.bathActivityFloatBtn: {//浮动
                        Toast.makeText(getApplicationContext(),"请等待后续开发",Toast.LENGTH_SHORT).show();
                       break;
                    }
                    case R.id.bath_activity_sublist: {//点击弹出悬浮列表
                        PopupMenu popupMenu = new PopupMenu(bain_marle_activity.this, arg0);
                        getMenuInflater().inflate(R.menu.bath_sublist_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.bathSublistDiagram: {
                                        Intent intent = new Intent(bain_marle_activity.this, series_Activity.class);
                                        intent.putExtra("subId", bath.getSubId());
                                        startActivity(intent);
                                        break;
                                    }
                                    case R.id.bathSublistHistory: {//历史记录
                                        Intent intent = new Intent(bain_marle_activity.this, deviceHistory.class);
                                        startActivity(intent);
                                        break;
                                    }
                                    case R.id.bathSublistSetting: {
                                        break;
                                    }
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                }
            }
        };
        dialog_cancel.setOnClickListener(button_Listener);
        dialog_yes.setOnClickListener(button_Listener);
        bath_activity_sublist.setOnClickListener(button_Listener);
        dragFloatActionButton.setOnClickListener(button_Listener);
        on_off_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//打开水浴锅开关
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("cmd", "on_off");

                } catch (JSONException e) {
                }
                if(isChecked){
                    bath.setStatus(1);
                }else{
                    bath.setStatus(0);
                }
                String str = obj.toString();
                MQTTService.publish(bath.getSubId(), str);
                stopTimeAsynctask();//停止计时
            }
        });
        sheding.setOnClickListener(new View.OnClickListener() {//设定功能
            @Override
            public void onClick(View v) {//设定按钮
                //String[] appoint_str = appoint_selector.getText().toString().split(":");
                if (bath.getStatus() == 0) {
                    Toast.makeText(getApplicationContext(), "请先打开设备", Toast.LENGTH_SHORT).show();
                    return;
                }
                stopTimeAsynctask();//关掉计时器
                String[] sv_time_str = time_selector.getText().toString().split(":");
                String sv_str = sv_selector.getText().toString();
                Double sv = Double.parseDouble(sv_str.substring(0, sv_str.indexOf("ºC")));
                Double st=getValue(sv_time_str);
                if(sv==0){
                    Toast.makeText(getApplicationContext(),"请设定温度",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(st==0){
                    Toast.makeText(getApplicationContext(),"请设定时间",Toast.LENGTH_SHORT).show();
                    return;
                }
                bath.setSv(sv);//设定温度
                bath.setSt(st);//设定时间
                hint_text.setVisibility(View.INVISIBLE);
                    //更新到数据库
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                            JSONObject sub_obj = new JSONObject();
                            JSONObject obj = new JSONObject();
                                sub_obj.put("SV", bath.getSv());
                            sub_obj.put("setTime", bath.getSt());
                            sub_obj.put("appointment", 0.0);
                            obj.put("work_process", sub_obj);
                            String str = obj.toString();
                            MQTTService.publish(bath.getSubId(), str);
                            JSONObject userJson = new JSONObject();
                            userJson.put("subId", bath.getSubId());
                            userJson.put("sv", bath.getSv());
                            userJson.put("st", bath.getSt());
                            postToTomcat.postByJson("deviceController/settingBath.json", JSON.toJSONString(userJson));
                         } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}.start();
                    startTimeAsynctask(bath.getSt());
            }
        });
        pause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//暂停功能
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(bath.getStatus()==0) {
                    Toast.makeText(getApplicationContext(), "请先打开设备", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bath.getSt()==0){
                    Toast.makeText(getApplicationContext(),"请设定时间或当前加热任务已结束",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isChecked) {
                    bath.setStatus(2);//暂停
                      double cv=bath.getCv();//获取当前温度
                      //设置目标温度为当前温度，目标时间无限，保存剩余加热时间
                       Publish(cv,60, 0);
                      String[] sv_time_str = time_selector.getText().toString().split(":");
                      double time=getValue(sv_time_str);//重新设定的时间，即为剩余时间,即读取时间
                      startTimeAsynctask(time);
                } else {
                    hint_text.setText("暂停中...");
                    //在此发送保存的状态给service方便重新开始时读取
                    stopTimeAsynctask();
                    /*
                       此过程用户可能重新选择了时间和温度
                     */
                    String[] sv_time_str = time_selector.getText().toString().split(":");
                    double time=getValue(sv_time_str);//重新设定的时间，即为剩余时间
                    double mianing=bath.getSt()-bath.getCt();//原来剩余时间
                    double cha=time-mianing;
                    bath.setSt(bath.getSt()+cha);//新的设定时间等于原来设定时间加上差值
                    Intent i = new Intent(bain_marle_activity.this, MQTTService.class);
                    startService(i);
                    Publish(bath.getCv(), 100, 0);
                    //剩余加热或者预约时
                }
            }
        });
        //选择器相关代码
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
                bath.setSv(integer + decimal / 100);
                tempterature_dialog.dismiss();
            }
        });
        temperature_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempterature_dialog.dismiss();
            }
        });
    }
    public void getBathStatus() {//获取设备状态信息

    }

    public void updateBath(Bath bath) throws IOException {//更新设备数据
        JSONObject object = new JSONObject();
        String url = null;
        Response response = postToTomcat.postJson(url, object.toString());
        if (response.body() != null) {

        }
    }
    public void startTimeAsynctask(Double time){//分钟
           timeAsynctask=new TimeAsynctask();
           timeAsynctask.execute(time);
    }
    public void stopTimeAsynctask(){
    if(timeAsynctask!=null&&!timeAsynctask.isCancelled()) {
        timeAsynctask.cancel(true);
        timeAsynctask = null;
     }
    }
  public class TimeAsynctask extends AsyncTask<Double, Integer, String>{
      /*
                在设定时调用
               */
      @Override
      protected void onPreExecute() {//执行后台耗时操作前被调用,通常用于进行初始化操作.
          super.onPreExecute();
      }
      @Override
      protected String doInBackground(Double... doubles) {
          double st=doubles[0];//分钟
          int s=(int)(st*60);//秒钟
          while (true){ //每隔一秒刷新一次
              if(isCancelled()){
                  break;
              }
              if(s%30==0){//每隔三十秒更新一次温度数据
                  FormBody.Builder builder=new FormBody.Builder();
                  builder.add("subId",bath.getSubId()+"");
                  try {
                      double cv=Double.parseDouble(postToTomcat.postFormData("deviceController/getCv.json",builder));
                      bath.setCv(cv);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
              if(s<=0)
                  break;
              s-=1;
              bath.setCt(bath.getSt()-(double)s/60);//更新剩余时间
              publishProgress(s);
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
         return null;
      }
      @Override
      protected void onPostExecute(String s) {//当doInBackground方法完成后,系统将自动调用此方法,
          // 并将doInBackground方法返回的值传入此方法.通过此方法进行UI的更新.
          //6
      }
      protected void onProgressUpdate(Integer... values) {//
          if(isCancelled()){
              return;
          }
          temperature.setText(bath.getCv()+"ºC");
          super.onProgressUpdate(values[0]);
          time_selector.setText(getTime(1,values[0]));
      }
      }
    public String getTime(int tag,double time){
        /*把时间转换成规定格式
           tag=0   分钟，tag=1，秒钟
        */ SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
        int hour=0;
        int minute =0;
        int second =0;
        Date date = new Date();
        switch (tag){
              case 0:{
                  hour = (int) (time/60);
                  minute = (int) (time %60);
                  second = (int)((time-hour*60-minute)*60);
                  break;
              }
             case 1:{
                  hour = (int) (time/3600);
                  minute = (int) (time-hour*3600)/60;
                  second = (int)((time-hour*3600-minute*60));
                  break;
              }
        }
        date.setHours(hour);
        date.setMinutes(minute);
        date.setSeconds(second);
        return ft.format(date);
    }
    public void uploadCoordinate(){//更新坐标点
        //数据结构"(2,2),(3,4.0)"

    }

}
