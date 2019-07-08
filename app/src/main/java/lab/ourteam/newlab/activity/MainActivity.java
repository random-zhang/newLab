package lab.ourteam.newlab.activity;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lab.ourteam.newlab.Adapter.myAdapter;
import lab.ourteam.newlab.Bean.listviewBean;
import lab.ourteam.newlab.MQTTMessage;
import lab.ourteam.newlab.service.MQTTService;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.fragment.fg_people;
import lab.ourteam.newlab.fragment.fg_center;
import lab.ourteam.newlab.fragment.fg_contrl;
import lab.ourteam.newlab.fragment.fg_news;
import lab.ourteam.newlab.myFragmentPagerAdapter;
import lab.ourteam.newlab.result_bridge;

public class MainActivity extends AppCompatActivity  {
    private String TAG = "MQTTService";
    private static final String SETTINGS_ACTION="android.settings.APPLICATION_DETAILS_SETTINGS";
    private List<listviewBean> mList = new ArrayList<>();
    private ListView mListView;
    private DrawerLayout mDrawerLayout;
    private long lastBack = 0;
    private View warning_view;
    private AlertDialog warning_dialog;
    private ImageView left_menu;
    private myAdapter adapter;
    private Button warning_cancel;
    private Button warning_allow;
    private double sv;
    private double cv=0;
    private long time_remaining;
    private boolean isAppoint;//选择appoint或time
    private List<Fragment> mFragmentList=new ArrayList<>();
    private myFragmentPagerAdapter myFragmentAdapter;
    private MenuItem menuItem;
    ViewPager viewPager;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mNavigationItemListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.menu_center:{
                    viewPager.setCurrentItem(2);
                   return true;
                }
                case R.id.menu_contrl:{
                    viewPager.setCurrentItem(0);
                    return true;
                }
                case R.id.menu_news:{
                    viewPager.setCurrentItem(1);
                    return true;
                }
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cv=0;
        sv=0;
        time_remaining=0;
        isAppoint=false;
        //注册EventBus
        Log.d(TAG, "oncreate");
        startService(new Intent(this, MQTTService.class));
        LayoutInflater LI=MainActivity.this.getLayoutInflater();
        AlertDialog.Builder warning_builder=new AlertDialog.Builder(MainActivity.this);
        warning_view=LI.inflate(R.layout.warning,null,false);
        warning_builder.setView(warning_view);
        warning_builder.setCancelable(true);
        warning_dialog=warning_builder.create();
        warning_dialog.setCanceledOnTouchOutside(false);
        if(!isNotificationEnable(this)){
            warning_dialog.show();
            //设置在底部
            Window window = warning_dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay();//获取屏幕的宽和高
            WindowManager.LayoutParams p = warning_dialog.getWindow().getAttributes();//获取对话框当前的参数值
            p.width = d.getWidth();
            warning_dialog.getWindow().setAttributes(p);//生效
        }
        warning_cancel=(Button)warning_view.findViewById(R.id.warining_cancel);
        warning_allow=(Button)warning_view.findViewById(R.id.warning_allow);
        initList();
        initId();
       initData();
        initListener();
        initListView();
        setNavigation();
        //监听区
        left_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }
       public void initListView(){
       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //模拟两条数据
            switch (position) {
                case 0://切换到测试
                {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, store.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
                case 1://切换到设置图
                {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, setting_Activity.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
                case 2://切换到关于视图
                {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, about_Activity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    });
}
    private void initList() {
        mList.add(new listviewBean(R.mipmap.store, "测试", 1));
        mList.add(new listviewBean(R.mipmap.edit, "设置", 2));
        mList.add(new listviewBean(R.mipmap.about, "关于", 3));
    }
    public void setNavigation(){
        navigation.setOnNavigationItemSelectedListener(mNavigationItemListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {//int position
                if(menuItem!=null){
                    menuItem.setChecked(false);
                }else{
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem=navigation.getMenu().getItem(i);
                menuItem.setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int i) {//int state

            }
        });
        mFragmentList.add(new fg_contrl());
        mFragmentList.add(new fg_news());
        mFragmentList.add(new fg_center());
        myFragmentAdapter=new myFragmentPagerAdapter(getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(myFragmentAdapter);
        viewPager.setOffscreenPageLimit(3);//设置缓存个数
        viewPager.setCurrentItem(0);
    }
    public void initId() {
        mListView = (ListView) findViewById(R.id.left_listview);
        adapter = new myAdapter(this, mList,R.layout.content_item);
        mListView.setAdapter(adapter);
        left_menu = (ImageView) findViewById(R.id.leftmenu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigation=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
    }
    private boolean isNotificationEnable(Context context){
        AppOpsManager mAppoOps=(AppOpsManager)context.getSystemService(APP_OPS_SERVICE);//获取实例
        ApplicationInfo appInfo=context.getApplicationInfo();
        String pkg=context.getApplicationContext().getPackageName();
        int uid=appInfo.uid;
        Class appOpsClass=null;//Context.APP_OPS_MANAGER
        try{
            appOpsClass=Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod=appOpsClass.getMethod("checkOpNoThrow",Integer.TYPE,Integer.TYPE,String.class);
            Field opPostNotificationValue=appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
            int value=(int)opPostNotificationValue.get(Integer.class);
            return ((int)checkOpNoThrowMethod.invoke(mAppoOps,value,uid,pkg)==AppOpsManager.MODE_ALLOWED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        if (mDrawerLayout.isDrawerOpen(findViewById(R.id.left_drawerlayout)))
            mDrawerLayout.closeDrawers();
        else
            super.onBackPressed();

    }
    public void initData(){
    }
    private void initListener() {//监听
        View.OnClickListener button_Listener=new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                switch(arg0.getId()){
                    case R.id.warining_cancel:{
                        warning_dialog.dismiss();
                        break;
                    }
                    case R.id.warning_allow:{
                        warning_dialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
                            Intent intent = new Intent()
                                    .setAction(SETTINGS_ACTION)
                                    .setData(Uri.fromParts("package", getApplicationContext().getPackageName(), null));
                            startActivity(intent);
                            return;
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Intent intent = new Intent().setAction(SETTINGS_ACTION)
                                    .setData(Uri.fromParts("package", getApplicationContext().getPackageName(), null));
                            startActivity(intent);
                            return;
                        }
                        break;
                    }
                }
            }
        };
        warning_cancel.setOnClickListener(button_Listener);
        warning_allow.setOnClickListener(button_Listener);
    }
@Override
    protected void onDestroy(){
        super.onDestroy();

}

}
