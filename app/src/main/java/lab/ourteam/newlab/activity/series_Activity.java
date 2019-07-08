package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Bean.Coordinates;
import lab.ourteam.newlab.Utils.postToTomcat;
import lab.ourteam.newlab.R;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.FormBody;

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
    private float sv;
    private float st;
    private int subId;
    private Intent intent;
    private initUITask task;
    private int intervalX;//x轴间隔时间
    private int intervalY;//y轴间隔时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.series_activity);
        initId();
        try{
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this) ;
            intervalX=Integer.parseInt(prefs.getString("axisX_setting","0"));
            intervalY=Integer.parseInt(prefs.getString("axisY_setting","0"));
        }catch (Exception e){
            e.printStackTrace();
        }
        intent = this.getIntent();
        subId=intent.getIntExtra("subId",0);
        initUI();
        initListener();
    }
    public void initUI(){
       // task=new initUITask(initUITask.INIT_UI);
        if(subId==0){
            finish();//打开页面失败
            Toast.makeText(this,"打开页面失败",Toast.LENGTH_SHORT).show();
        }
        //task.execute(subId);
        /*
         模拟数据
         */
        sv=100;st=100;
        Toast.makeText(getApplicationContext(),"模拟数据",Toast.LENGTH_SHORT).show();
        pointValues=new ArrayList<>();
        for(int i=0;i<100;i++){
            pointValues.add(new PointValue(i,(float)Math.random()*i));
        }
            fillChart((float)10.1,10);
    }
    private Viewport initViewPort(float left, float right,float top) {
        Viewport port = new Viewport();
        port.top =top;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = 0;//Y轴下限，固定
        port.left = left;//X轴左边界，变化
        port.right = right;//X轴右边界，变化
        return port;
    }
    private void initId(){//分配ID
        lineChartView=(LineChartView)findViewById(R.id.lineChartView);
        returnMenu=(ImageView)findViewById(R.id.series_return_menu);
    }
    private void initListener(){//监听
        returnMenu.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
    }
    public Coordinates getCoordinates(int subId) throws IOException {//数据库操作
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("subId", subId + "");
        String response = postToTomcat.postFormData("deviceController/getCoordinates.json", builder);
        Log.v("调试", response);
        Coordinates coordinates = null;
        if (response == null)
            return null;
        else {
        }
           /* JSONArray detail=JSONObject.parseArray(response);
            for (int i=0; i<detail.size();i++){
                if(detail.get(i)!=null||!detail.get(i).equals("")){
                    JSONArray detailChild =detail.getJSONArray(i);
                    detail.get
                }*/
            JSONObject jsonObject=JSON.parseObject(response);
            double sv=jsonObject.getDouble("sv");
            double st=jsonObject.getDouble("st");
            JSONArray jsonArray=jsonObject.getJSONArray("coordinates");
            coordinates=new Coordinates();
            coordinates.setSt(st);
            coordinates.setSv(sv);
            coordinates.setCoordinates(jsonArray.toJavaList(Coordinates.Point.class));
            //coordinates = new Coordinates(st,sv,jsonArray.toString());
            //coordinates=JSON.parseObject(response,Coordinates.class);
          //  Log.v("调试", jsonArray+"");
            return coordinates;
        }
    public Coordinates.Point getCoordinate(int subId) throws IOException {//数据库操作
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("subId",subId+"");
        String response=postToTomcat.postFormData("deviceController/getCoordinate.json",builder);
        return (response==null)?null: JSON.parseObject(response, Coordinates.Point.class);
    }
    public class initUITask extends AsyncTask<Integer,Float,Coordinates>{
        private int tag;
        public static final int INIT_UI=0,UPDATE_UI=1;
        initUITask(int tag) {//tag=0  初始化UI,tag=1 更新UI
            this.tag=tag;
        }
        @Override
        protected void onPreExecute() {//执行后台耗时操作前被调用,通常用于进行初始化操作.
            super.onPreExecute();
        }
        @Override
        protected Coordinates doInBackground(Integer... integers) {
            int subId=integers[0];
            //从服务器获取数据
            Coordinates coordinates=null;
            Coordinates.Point point=null;
            if(tag==INIT_UI){
                try {
                    coordinates=getCoordinates(subId);
                    Log.v("调试",coordinates.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }else if(tag==UPDATE_UI) {//每隔一段时间更新一次
                while(true){
                    try {
                        Thread.sleep(10000);
                        point=getCoordinate(subId);
                        publishProgress((float)point.getCt(),(float)point.getCv());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            return coordinates;
        }
        @Override
        protected void onProgressUpdate(Float... values) {//
              fillChartByCoordinate(values[0],values[1]);
        }
        @Override
        protected void onPostExecute(Coordinates coordinates) {//当doInBackground方法完成后,系统将自动调用此方法,
            Log.i("调试","运行到此步1");
            fillChartByCoordinates(coordinates);
            Toast.makeText(series_Activity.this,"坐标"+coordinates.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void fillChartByCoordinates(Coordinates coordinates){//填充图表,更新UI
        sv=(float)coordinates.getSv();
        st=(float)coordinates.getSt();
        Coordinates.Point cPoint=coordinates.getCurrentPoint();
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
        List<Coordinates.Point> points=coordinates.getCoordinates();
        pointValues=new ArrayList<>();
        for(Coordinates.Point point:points){
            pointValues.add(new PointValue((float)point.getCt(),(float)point.getCv()));
        }
        if(cPoint!=null)
           fillChart((float)cPoint.getCt(),(float)cPoint.getCv());
    }
    public void fillChartByCoordinate(float ct,float cv) {//填充图表,更新UI
        pointValues.add(new PointValue(ct,cv));
        fillChart(ct,cv);
    }
    public void fillChart(float ct,float cv){
        Log.i("调试","运行到此步3");
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
        Log.i("调试",pointValues.toString());
        Line line = new Line(pointValues);
        line.setCubic(true);
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setColor(getResources().getColor(R.color.colorAccent));
        line.setShape(ValueShape.CIRCLE);
        line.setFormatter(chartValueFormatter);
        lineList=new ArrayList<>();
        lineList.add(line);
        chartData=new LineChartData(lineList);
        //坐标系的刻画
        List<AxisValue> axisValueX=new ArrayList<>();
        List<AxisValue> axisValueY=new ArrayList<>();
        int intervalx=intervalX;
        int intervaly=intervalY;
        if(intervalX==0){//智能配置模式
            intervalx=(int)(st/10);
        }
        if (intervalY==0){//
            intervaly=(int)(sv/10);
        }
        for(int i=0;i<=sv/intervaly;i++){
            axisValueY.add(new AxisValue(i).setValue(i*intervaly));
        }
        for(int i=0;i<=st/intervalx;i++){
            axisValueX.add(new AxisValue(i).setValue(i*intervalx));
        }
        Axis axisX = new Axis();
        Axis axisY = new Axis();//.setHasLines(true);
        axisX.setValues(axisValueX);
        axisY.setValues(axisValueY);
        axisX.setName("时间/min");
        axisY.setName("温度/C");
        chartData.setAxisYLeft(axisY);
        chartData.setAxisXBottom(axisX);
        Viewport port ;
        if(cv==0&&ct==0){//还没开始打点
            port=initViewPort(0, 20,sv);
        }else{
            if(ct>10)
                port=initViewPort(ct-10, ct+10,sv);
            else
                port=initViewPort(0, 20-ct,sv);
        }
        Viewport maxport = initViewPort(0, st,sv);
        lineChartView.setLineChartData(chartData);
        lineChartView.setMaximumViewport(maxport);
        lineChartView.setCurrentViewport(port);//动态处理此行
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setZoomEnabled(true);
        lineChartView.setVisibility(View.VISIBLE);
    }
}
