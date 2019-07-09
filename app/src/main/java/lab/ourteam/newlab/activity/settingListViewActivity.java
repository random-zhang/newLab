package lab.ourteam.newlab.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Adapter.settingListViewAdapter;
import lab.ourteam.newlab.Bean.settingListViewBean;
import lab.ourteam.newlab.R;

import static com.mob.MobSDK.getContext;

public class settingListViewActivity extends AppCompatActivity {//设置页
   private List<settingListViewBean> beans;
   private settingListViewAdapter adapter;
   private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list_view);
        initId();
        initListView();
        initListener();
        initUI();
    }
    private void initId(){
        recyclerView=findViewById(R.id.setting_list_view_activity_recylerview);
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

           }
       });
    }
    private void initUI(){

    }
    private  List<settingListViewBean> createBeans(){
        List<settingListViewBean> beans=new ArrayList<>();
        beans.add(new settingListViewBean("账户设置",R.mipmap.arrow));//-1代表无图
        beans.add(new settingListViewBean("系统设置",R.mipmap.arrow));
        return beans;
    }
}
