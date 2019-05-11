package lab.ourteam.newlab.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lab.ourteam.newlab.Adapter.cardViewAdapter;
import lab.ourteam.newlab.R;

public class surroundDevices extends AppCompatActivity implements Iactivity {
    private RecyclerView recyclerView;
    private cardViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surround_devices);
        initID();
    }

    @Override
    public void initID() {
        recyclerView=findViewById(R.id.surroundDevicesActivity_recyclerView);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new cardViewAdapter(this);
        recyclerView.setAdapter(adapter);
        //adapter.addNewItem("新设备",R.color.cardView);
        linearLayoutManager.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {

    }
    public void addDevices(){//把周围存在的设备做成卡片加入列表中，

    }
}
