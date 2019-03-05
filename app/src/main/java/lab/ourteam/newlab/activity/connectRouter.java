package lab.ourteam.newlab.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lab.ourteam.newlab.Bean.devicebean;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.WifiAutoConnectManager;

import static lab.ourteam.newlab.Constant.connectRouter_result_code;
import static lab.ourteam.newlab.WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS;

public class connectRouter extends AppCompatActivity implements Iactivity{//连接设备路由器页面公共类
    private RelativeLayout ssidTeam,progressTeam;//点击进行wifi连接,连接进度
    private Button finish;
    private ImageView back;
    private WifiAutoConnectManager manager;
    private TextView ssidView;//显示ssid
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_router);
        initID();
        manager=new WifiAutoConnectManager(this);
        if(!manager.isOpen())      manager.openWifi();
        String ssid=manager.getConfigSsid("device1");//ssid是否匹配
        if (ssid!=null){
            ssidTeam.setVisibility(View.VISIBLE);
            ssidView.setText(ssid);
        }

    }

    @Override
    public void initID() {
          finish=findViewById(R.id.activity_connect_router_finish);
          ssidTeam=findViewById(R.id.activity_connect_router_ssid_team);
          progressTeam=findViewById(R.id.activity_connect_router_progress_team);
          back=findViewById(R.id.activity_connect_router_return_menu);
          ssidView=findViewById(R.id.activity_connect_router_ssid);
          webView=findViewById(R.id.activity_connect_router_webview);

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.activity_connect_router_finish:{//完成连接
               finish();
               break;

           }
           case R.id.activity_connect_router_ssid_team:{//点击进行连接
               manager.connect(ssidView.getText().toString(),null,WIFICIPHER_NOPASS);
               //如果连接上设备路由器,打开网页进行配置
               webView.loadUrl("https://192.168.4.1");
               webView.setWebViewClient(new WebViewClient() {
                   @Override
                   public boolean shouldOverrideUrlLoading(WebView view, String url) {
                       return false;
                   }
               });
               break;
           }
           case R.id.activity_connect_router_return_menu:{
               finish();
               break;
           }
       }
        finish.setOnClickListener(this);
        ssidTeam.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("device", new devicebean(1,"水浴锅"));//返回整个设备信息
        setResult(connectRouter_result_code, intent);
        super.finish();
    }
}
