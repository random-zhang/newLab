package lab.ourteam.newlab.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import lab.ourteam.newlab.Bean.CardViewBean;
import lab.ourteam.newlab.Bean.devicebean;
import lab.ourteam.newlab.Bean.listviewBean;
import lab.ourteam.newlab.R;

import static lab.ourteam.newlab.Constant.wifiConfig_activity_result_code;

public class wifiConfigActivity extends AppCompatActivity implements Iactivity {//扫描二维码情况下wifi配置页面
    private  WebView webView;
    private ImageView returnMenu;
    private TextView finish;
    private Button refresh_web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);
        initID();
        webView.loadUrl("http://192.168.4.1");//连上设备wifi后打开设备wifi配置页面
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }
    @Override
    public void initID() {
        webView=findViewById(R.id.testwebview);
        returnMenu=findViewById(R.id.wifiConfig_activity_return_menu);
        finish=findViewById(R.id.wifiConfig_activity_finish);
        returnMenu.setOnClickListener(this);
        finish.setOnClickListener(this);
        refresh_web=findViewById(R.id.refresh_web);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.wifiConfig_activity_return_menu:{
               break;
           }
           case R.id.wifiConfig_activity_finish:{
               //如果成功完成配置，则在fg_contrl页面添加设备信息
               //发送指定设备信息给上个页面,并关闭当前页面
               Intent intent=new Intent();
               intent.putExtra("status",1);
               // devicebean bean=new devicebean(1,"水浴锅");
               //intent.putExtra("deviceMAC",getIntent().getStringExtra("deviceMAC"));
               setResult(wifiConfig_activity_result_code,intent);
               finish();
               break;
           }
           case R.id.refresh_web:{
               webView.reload(); //刷新页面
               break;
           }
       }
    }
}
