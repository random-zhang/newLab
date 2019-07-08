package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lab.ourteam.newlab.Adapter.deviceListViewAdapter;
import lab.ourteam.newlab.Adapter.listAdapter;
import lab.ourteam.newlab.R;

import static com.mob.MobSDK.getContext;

public class userDevices extends Activity implements View.OnClickListener {
    private RecyclerView  recycler_cardview;
    private listAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_devices);
        recycler_cardview=findViewById(R.id.activity_user_devices_recyclerView);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_cardview.setLayoutManager(linearLayoutManager);
        adapter=new listAdapter(this);
        recycler_cardview.setAdapter(adapter);
        adapter.addNewItem("我管理的设备");
        adapter.addNewItem("我加入的设备");
        adapter.setOnItemClickListener(new listAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {//给每条添加点击事件，设备id号为唯一识别号

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
