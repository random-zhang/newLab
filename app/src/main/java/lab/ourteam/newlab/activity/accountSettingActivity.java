package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Adapter.settingListViewAdapter;
import lab.ourteam.newlab.Bean.settingListViewBean;
import lab.ourteam.newlab.R;

import static com.mob.MobSDK.getContext;
import static lab.ourteam.newlab.Utils.saveToLocation.clearUserInfo;
import static lab.ourteam.newlab.Utils.saveToLocation.updateLoginStatus;

public class accountSettingActivity extends Activity {

    private List<settingListViewBean> beans;
    private settingListViewAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        initId();
        initListView();
        initListener();
        initUI();
    }

    private void initId(){
        recyclerView=findViewById(R.id.account_setting_activity_recylerview);
    }
    private void initListView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        beans=createBeans();
        adapter=new settingListViewAdapter(getApplicationContext(),beans);
        recyclerView.setAdapter(adapter);

    }
    private void initListener(){
        adapter.setOnItemClickListener(new settingListViewAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View v, int position) {
                switch(position){
                    case 0:{//退出登录
                        updateLoginStatus(false,getContext());
                        //清除本地用户信息缓存
                        clearUserInfo(getContext());
                        Toast.makeText(getContext(),"成功退出",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
    }
    private void initUI(){

    }
    private  List<settingListViewBean> createBeans(){
        List<settingListViewBean> beans=new ArrayList<>();
        beans.add(new settingListViewBean("退出登录",-1));//-1代表无图
        return beans;
    }
}
