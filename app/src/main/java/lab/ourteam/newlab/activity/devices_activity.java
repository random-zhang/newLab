package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;

import lab.ourteam.newlab.Adapter.dCardAdapter;
import lab.ourteam.newlab.Bean.Bath;
import lab.ourteam.newlab.Bean.CardViewBean;
import lab.ourteam.newlab.Bean.Device;
import lab.ourteam.newlab.Constant;
import lab.ourteam.newlab.DragFloatActionButton;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.WifiAutoConnectManager;

import static lab.ourteam.newlab.Constant.devices_activity_result_code;
import static lab.ourteam.newlab.Constant.wifiConfig_activity_result_code;
import static lab.ourteam.newlab.WifiAutoConnectManager.WifiCipherType.*;

public class devices_activity extends Activity implements Iactivity{
    private ImageView returnMenu,addDevices;
    private WifiAutoConnectManager wifiAdmin;
    private View wifiConfiView;
    private Dialog wifiConfiDialog;
    private RecyclerView devicesActivityRecyclerView;
    private TextView sDevices;
    private Device device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_activity);
        initID();
        wifiAdmin=new WifiAutoConnectManager(this);
        addDevices.setOnClickListener(this);
        returnMenu.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
        DragFloatActionButton dragFloatActionButton= (DragFloatActionButton) findViewById(R.id.floatBtn);
        dragFloatActionButton.setOnClickListener(this);
    }

    @Override
    public void initID() {
        addDevices=findViewById(R.id.addDevices);
        returnMenu=findViewById(R.id.devices_activity_return_menu);
        devicesActivityRecyclerView=findViewById(R.id.devices_activity_recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        devicesActivityRecyclerView.setLayoutManager(gridLayoutManager);
        dCardAdapter adapter=new dCardAdapter(this);//设置适配的卡片
        devicesActivityRecyclerView.setAdapter(adapter);
        sDevices=findViewById(R.id.devices_activity_surroundDevices);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addDevices: {//点击添加设备图标
                new IntentIntegrator(this).setCaptureActivity(qrcode_activity.class).initiateScan();
                break;
            }
            case R.id.floatBtn: {//浮动框
                PopupMenu popupMenu = new PopupMenu(this, v);
                getMenuInflater().inflate(R.menu.pop_item, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_last:
                                Toast.makeText(devices_activity.this, "" + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_next:
                                Toast.makeText(devices_activity.this, "" + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                Log.e("****--->", "float");
                // Toast.makeText(this,"flaot---",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.devices_activity_surroundDevices:{//打开gps，搜索周围存在的设备

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Constant.devices_activity_request_code&&resultCode==wifiConfig_activity_result_code){
            int status=(int)data.getIntExtra("status",0);//默认为失败
            Intent intent=new Intent();
            Device device=new Device();
            if(status==1){//wifi配置成功,把二维码携带设备信息传送给控制页面
                intent.putExtra("device",(Serializable)device);
            }else{
                Toast.makeText(this,"wifi配置失败!",Toast.LENGTH_SHORT);
            }
            setResult(devices_activity_result_code,intent);
            finish();
            return;
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);//二维码页面返回的数据
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {//收到扫描结果
                /**
                 * 二维码应该包含 ssid password type  设备唯一编号  设备类编号
                 *  eg: deviceName:水浴锅;S:m1998cc;P:123456;T:wep;MAC:19990777;deviceId:1;
                 */
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String strResult=result.getContents();
                if (strResult.contains("P:") && strResult.contains("T:")) {// 自动连接wifi
                    Log.e("扫描返回的结果----->", strResult);// 还是要判断
                    String deviceNameTemp = strResult.substring(strResult.indexOf("deviceName:"));
                    final  String deviceName = deviceNameTemp.substring(2,deviceNameTemp.indexOf(";"));
                    String passwordTemp = strResult.substring(strResult.indexOf("P:"));
                    final  String password = passwordTemp.substring(2,passwordTemp.indexOf(";"));
                    String netWorkTypeTemp = strResult.substring(strResult.indexOf("T:"));
                    final String netWorkType = netWorkTypeTemp.substring(2,netWorkTypeTemp.indexOf(";"));
                    String netWorkNameTemp = strResult.substring(strResult.indexOf("S:"));
                    final String netWorkName = netWorkNameTemp.substring(2,netWorkNameTemp.indexOf(";"));
                    String deviceMACTemp = strResult.substring(strResult.indexOf("MAC:"));
                    final int  deviceMAC = Integer.parseInt(deviceMACTemp.substring(2,deviceMACTemp.indexOf(";")));
                    String deviceIdTemp = strResult.substring(strResult.indexOf("deviceId:"));
                    final int deviceId = Integer.parseInt(deviceIdTemp.substring(2,deviceIdTemp.indexOf(";")));
                    Dialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle("扫描到可用wifi")
                            .setIcon(R.mipmap.wifi)
                            .setMessage("wifi名：" + netWorkName)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) { }})
                            .setPositiveButton("加入此wifi ",new DialogInterface.OnClickListener() {
                                @Override public void onClick (DialogInterface dialog, int which) {
                                            //int net_type = 0x13;
                                     WifiAutoConnectManager.WifiCipherType net_type=null;
                                            if (netWorkType.compareToIgnoreCase("wpa") == 0) {
                                                net_type =WIFICIPHER_WPA;// wpa
                                            } else if (netWorkType.compareToIgnoreCase("wep") == 0) {
                                                net_type =WIFICIPHER_WEP;// wep
                                            } else {
                                                net_type =WIFICIPHER_NOPASS;// 无加密
                                            }
                                            wifiAdmin.connect(netWorkName, password, net_type);
                                            Log.e("解析的数据----->", "networkname: " + netWorkName + " " + "password: "
                                                            + password + " netWorkType: " + net_type);
                                            //把设备编号和类编号发送传递到wifi配置页面
                                            //连上设备id的wifi后打开配置页面
                                    device=new Device();
                                    device.setDeviceid(deviceId);
                                    device.setWssid(netWorkName);
                                    device.setSuid(deviceMAC);
                                    device.setDevicename(deviceName);
                                    device.setRunning(false);
                                             Intent intent=new Intent(devices_activity.this,wifiConfigActivity.class);
                                             startActivityForResult(intent, Constant.devices_activity_request_code);
                                }
                                    }).create();
                    alertDialog.show();
                } }
            }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
