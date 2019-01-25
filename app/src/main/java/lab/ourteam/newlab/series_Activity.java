package lab.ourteam.newlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class series_Activity extends Activity {
    private ImageView returnMenu;
    private LineChartView lineChartView;
    private LineChartData chartData;
    private  List<PointValue> pointValues;
    private List<Line>lineList;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private Axis axisX;
    private Axis axisY;
    private double cv;
    private int cur_time;
    private int first_start_time;
    private int k=10;
    private int j=0;
    private EventBus eventBus;
    private int cur_sv;
    private TextView series_sv_text;
    private ImageView series_sv_minus;
    private ImageView series_sv_plus;
    private TextView series_time_text;//剩余加热时间
    private ImageView series_time_minus;
    private ImageView series_time_plus;
    private int surplus_time;
    private int already_time=0;
    private Intent intent;
    private boolean isAppoint=true;
    private LinearLayout series_sv_team;
    private LinearLayout series_time_team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.series_activity);
        initId();
        intent = this.getIntent();
        cur_sv=intent.getIntExtra("SV",0);//获取当前设定的温度
        cur_time=intent.getIntExtra("set_time",0);
        isAppoint=intent.getBooleanExtra("isAppoint",true);
        /**
         * 此时隐藏按钮
         */
        if(isAppoint){
            series_sv_team.setVisibility(View.INVISIBLE);
            series_time_team.setVisibility(View.INVISIBLE);
        }else{
            series_sv_team.setVisibility(View.VISIBLE);
            series_time_team.setVisibility(View.VISIBLE);
        }
        String str1=String.format("%d",cur_sv);
        series_sv_text.setText(str1);

        String str2=String.format("%d",cur_time);
        series_time_text.setText(str2);
        returnMenu.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
        initListener();
        //注册EventBus
        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        lineList = new ArrayList<Line>();
        Line line = new Line(pointValues);
        line.setCubic(true);
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setColor(getResources().getColor(R.color.colorAccent));
        line.setShape(ValueShape.CIRCLE);
        lineList.add(line);

        chartData=new LineChartData(lineList);
        List<AxisValue> axisValueX=new ArrayList<>();
        List<AxisValue> axisValueY=new ArrayList<>();
        for(int i=0;i<=100;i++){
            axisValueX.add(new AxisValue(i).setValue(i));
            axisValueY.add(new AxisValue(i).setValue(i));
        }
        Axis axisX = new Axis();
        Axis axisY = new Axis();//.setHasLines(true);
        axisX.setValues(axisValueX);
        axisY.setValues(axisValueY);
        axisX.setName("时间");
        axisY.setName("温度");
        chartData.setAxisYLeft(axisY);
        chartData.setAxisXBottom(axisX);
        Viewport port = initViewPort(0, 10);
        Viewport maxport = initViewPort(0, 100);
        lineChartView.setLineChartData(chartData);
        lineChartView.setMaximumViewport(maxport);
        lineChartView.setCurrentViewport(port);

    }
    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 100;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = 0;//Y轴下限，固定
        port.left = left;//X轴左边界，变化
        port.right = right;//X轴右边界，变化
        return port;
    }
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getpointValueMessage(pointValueMessage pointvalueMessage) {
        List<PointValue> pointValues = pointvalueMessage.getPointValueList();
       // already_time=(int)pointValues.get(pointValues.size()-1).getX();//已经历时间
        //urplus_time=intent.getIntExtra("set_time",0)-already_time;//获取剩余时间
        //String str2=String.format("%d",surplus_time);
        //series_time_text.setText(str2);
        boolean isDraw=pointvalueMessage.getIsDraw();
        if(isDraw){
            Toast.makeText(this,"接收到isDraw"+isDraw,Toast.LENGTH_SHORT).show();
        }
        j=pointvalueMessage.getJ();
        k=pointvalueMessage.getK();
        lineList = new ArrayList<Line>();
        Line line = new Line(pointValues);
        line.setCubic(true);
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setColor(getResources().getColor(R.color.colorAccent));
        line.setShape(ValueShape.CIRCLE);
        lineList.add(line);
        chartData = new LineChartData(lineList);
        List<AxisValue> axisValueX=new ArrayList<>();
        List<AxisValue> axisValueY=new ArrayList<>();
        for(int i=0;i<100;i++){
            axisValueX.add(new AxisValue(i).setValue(i));
            axisValueY.add(new AxisValue(i).setValue(i));
        }
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setValues(axisValueX);
        axisY.setValues(axisValueY);
        axisX.setName("时间");
        axisY.setName("温度");
        chartData.setAxisYLeft(axisY);
        chartData.setAxisXBottom(axisX);
        Viewport port = initViewPort(j, k);
        Viewport maxport = initViewPort(0, 100);
        lineChartView.setLineChartData(chartData);
        lineChartView.setMaximumViewport(maxport);
        lineChartView.setCurrentViewport(port);
    }
    /*@Subscribe(threadMode = ThreadMode.ASYNC)
    void onBridge(List<PointValue> pointvalues){
        Toast.makeText(this,"接收到service的消息",Toast.LENGTH_SHORT).show();
        pointValues=pointvalues;
        lineList = new ArrayList<Line>();
        Line line = new Line(pointValues);
        line.setCubic(true);
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setColor(getResources().getColor(R.color.colorAccent));
        line.setShape(ValueShape.CIRCLE);
        lineList.add(line);

        chartData=new LineChartData(lineList);
        List<AxisValue> axisValueX=new ArrayList<>();
        List<AxisValue> axisValueY=new ArrayList<>();
        for(int i=0;i<=100;i++){
            axisValueX.add(new AxisValue(i).setValue(i));
            axisValueY.add(new AxisValue(i).setValue(i));
        }
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setValues(axisValueX);
        axisY.setValues(axisValueY);
        axisX.setName("时间");
        axisY.setName("温度");
        chartData.setAxisYLeft(axisY);
        chartData.setAxisXBottom(axisX);
        Viewport port = initViewPort(0, 10);
        Viewport maxport = initViewPort(0, 100);
        lineChartView.setLineChartData(chartData);
        lineChartView.setMaximumViewport(maxport);
        lineChartView.setCurrentViewport(port);

    }
    @Override
    //解除注册
    protected void onDestroy(){
        super.onDestroy();
        if(eventBus.isRegistered(this)){
            eventBus.unregister(this);
        }
    }
*/
    private void initId(){//分配ID
        lineChartView=(LineChartView)findViewById(R.id.lineChartView);
        returnMenu=(ImageView)findViewById(R.id.series_return_menu);
        series_sv_text=(TextView)findViewById(R.id.series_sv_text);
        series_time_text=(TextView)findViewById(R.id.series_time_text);
        series_sv_minus=(ImageView)findViewById(R.id.series_sv_minus);
        series_sv_plus=(ImageView)findViewById(R.id.series_sv_plus);
        series_time_minus=(ImageView)findViewById(R.id.series_time_minus);
        series_time_plus=(ImageView)findViewById(R.id.series_time_plus);
        series_time_team=(LinearLayout)findViewById(R.id.series_time_team);
        series_sv_team=(LinearLayout)findViewById(R.id.series_sv_team);
    }
    private void initListener(){//监听

        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                String str=series_sv_text.getText().toString();
                String Str=series_time_text.getText().toString();
                int Sv=Integer.parseInt(str);
                int Time=Integer.parseInt(Str);
             switch (arg0.getId())
             {

                 case R.id.series_sv_minus:{
                     if(Sv>0) Sv-=1;

                     String str1=String.format("%d",Sv);
                     series_sv_text.setText(str1);
                     break;
                 }
                 case R.id.series_sv_plus:{
                     if(Sv<100) Sv+=1;
                     String str1=String.format("%d",Sv);
                     series_sv_text.setText(str1);
                     break;
                 }
                 case R.id.series_time_minus:{
                     if(Time>0) Time-=1;
                     String str2=String.format("%d",Time);
                     series_time_text.setText(str2);
                     break;
                 }
                 case R.id.series_time_plus:{
                     if(Time<100) Time+=1;
                     String str2=String.format("%d",Time);
                     series_time_text.setText(str2);
                     break;
                 }
             }
             Intent i=new Intent(series_Activity.this,MQTTService.class);
             i.putExtra("class_selector",class_selector.series_Activity);
             Toast.makeText(series_Activity.this,"sv"+Sv,Toast.LENGTH_SHORT).show();
             i.putExtra("series_time",Time);
             i.putExtra("isdraw",true);
             i.putExtra("isSeries_sv",true);
             i.putExtra("series_sv",Sv);
             startService(i);
                JSONObject sub_obj=new JSONObject();
                JSONObject obj=new JSONObject();
                try{
                    sub_obj.put("SV",Sv);
                    sub_obj.put("setTime",Time);
                    sub_obj.put("appointment",0);
                    obj.put("work_process",sub_obj);
                }catch(JSONException e){
                }
                String pubString=obj.toString();
                MQTTService.publish(pubString);
            }
        };
        series_sv_minus.setOnClickListener(listener);
        series_sv_plus.setOnClickListener(listener);
        series_time_minus.setOnClickListener(listener);
        series_time_plus.setOnClickListener(listener);
    }
}
