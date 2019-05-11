package lab.ourteam.newlab.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lab.ourteam.newlab.Adapter.cardViewAdapter;
import lab.ourteam.newlab.Adapter.deviceListViewAdapter;
import lab.ourteam.newlab.Bean.Bath;
import lab.ourteam.newlab.Bean.CardViewBean;
import lab.ourteam.newlab.Bean.Device;
import lab.ourteam.newlab.Bean.devicebean;
import lab.ourteam.newlab.Constant;
import lab.ourteam.newlab.R;
//import lab.ourteam.newlab.activity.device_activity;
import lab.ourteam.newlab.activity.bain_marle_activity;
import lab.ourteam.newlab.activity.devices_activity;

import static lab.ourteam.newlab.Constant.devices_activity_result_code;
import static lab.ourteam.newlab.Utils.postToTomcat.getBath;

public class fg_contrl extends Fragment implements View.OnClickListener{
    private View view;
    private RecyclerView recycler_cardview;
    private ImageView add_device;
    private deviceListViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fg_contrl,container,false);
        recycler_cardview=view.findViewById(R.id.id_recyclerView);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_cardview.setLayoutManager(linearLayoutManager);
         adapter=new deviceListViewAdapter(getContext());
        recycler_cardview.setAdapter(adapter);
        add_device=getActivity().findViewById(R.id.add_device);//添加设备
        add_device.setOnClickListener(this);
        /*设置条目点击事件*/

        adapter.setOnItemClickListener(new deviceListViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {//给每条添加点击事件，设备id号为唯一识别号
                /*
                     在此判断device的具体类型
                     连接服务器查询设备的详细信息
                 */
                Intent intent = null;
                switch (adapter.getDevice(position).getDeviceid()) {
                    case 00001: {//水浴锅
                        intent = new Intent(getContext(), bain_marle_activity.class);
                        Bath  bath= null;//从服务器获取设备具体信息填充到bain_marle_activity中
                        try {
                            bath = getBath(adapter.getDevice(position).getDeviceid(), adapter.getDevice(position).getSuid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"添加失败",Toast.LENGTH_SHORT);
                           break;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"添加失败",Toast.LENGTH_SHORT);
                            break;
                        }
                        intent.putExtra("bath",(Serializable)bath);
                    }
                }
                startActivityForResult(intent, Constant.fg_contrl_request_code);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== Constant.fg_contrl_request_code&&resultCode==devices_activity_result_code){//成功添加绑定设备后
            //获取添加的设备信息
            Device device=(Device)data.getSerializableExtra("device");
            if(device!=null) {
                adapter.addNewItem(device);//把设备添加到
                linearLayoutManager.scrollToPosition(0);
            }else{
                Toast.makeText(getContext(),"添加失败",Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.add_device) {
            Intent intent=new Intent(getContext(),devices_activity.class);
            startActivityForResult(intent, Constant.fg_contrl_request_code);//打开用户信息列表
        }
    }
}
