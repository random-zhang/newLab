package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import lab.ourteam.newlab.Adapter.listAdapter;
import lab.ourteam.newlab.R;

public class myDevices extends Activity implements  Iactivity{//我的设备
    private ImageView back;
    private RecyclerView recyclerView;


    private final String MY_MANAGE_DEVICES="我管理的设备",MY_JOIN_DEVICES="我接入的设备";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);
        initID();
        initRecyclerView();
    }

    @Override
    public void initID() {
        back=findViewById(R.id.myDevicesActivity_return_menu);
        recyclerView=findViewById(R.id.myDevicesActivity_recyclerView);

    }

    public void initRecyclerView(){//初始化RecyclerView
        listAdapter adapter;//基础列表适配器
        LinearLayoutManager linearLayoutManager;
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new listAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.addNewItem(MY_MANAGE_DEVICES);
        adapter.addNewItem(MY_JOIN_DEVICES);
        linearLayoutManager.scrollToPosition(0);
        adapter.setOnItemClickListener(new listAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {//每一个item对应的点击事件
                switch (position){
                    case 0:{
                        Intent intent=new Intent(myDevices.this,createManageActivity().getClass());
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent=new Intent(myDevices.this,createJoinActivity().getClass());
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    public Activity createManageActivity(){
        baseListActivity activity=new baseListActivity(MY_MANAGE_DEVICES,-10,R.mipmap.add);
        activity.setCreateActivity(new baseListActivity.CreateActivity() {
            @Override
            public void onclick() {

            }

            @Override
            public void initItem() {//通过网络获取用户管理的设备

            }

            @Override
            public RecyclerView.Adapter bindAdapter() {

                return null;
            }
        });
        return activity;
    }
    public Activity createJoinActivity(){//在此页面可以通过右上角的按钮deviceCodeActivity页面
        baseListActivity activity=new baseListActivity(MY_JOIN_DEVICES,R.layout.activity_device_code,R.mipmap.devicecode);
        activity.setCreateActivity(new baseListActivity.CreateActivity() {
            @Override
            public void onclick() {//每一项item对应的点击事件

            }

            @Override
            public void initItem() {//通过网络获取用户管理的设备

            }

            @Override
            public RecyclerView.Adapter bindAdapter() {

                return null;
            }
        });
        return activity;
    }
    @Override
    public void onClick(View v) {

    }
}
