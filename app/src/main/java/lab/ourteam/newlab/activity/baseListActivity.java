package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import lab.ourteam.newlab.Adapter.listAdapter;
import lab.ourteam.newlab.R;
/*
    基础列表activity
    提供返回上一层的功能
    右上角提供自定义功能
    中心页面以列表矿的形式呈现
 */
public class baseListActivity extends Activity implements View.OnClickListener{//
    private ImageView back;
    private ImageView customButton;
    private TextView titleView;
    private String title;
    //private IBaseListAdapter adapter;
    private int activityId,customButtonId;//对应定制按钮的跳转activity
    private RecyclerView recyclerView;
    public CreateActivity createActivity;
    public baseListActivity(String title,int activityId,int customButtonId){//当activityId为-10时，代表无跳转activity
        /*
            需要
            custom对应的activity
            custom对应图片
            title对应文字
            对应的adapter
         */
        this.title=title;
        //this.adapter=adapter;
        this.activityId=activityId;
        this.customButtonId=customButtonId;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        initID();
        initUI();
    }
        public void initID() {//继承的子类只需要重写此方法，即可实现替换
            back = findViewById(R.id.baseListActivity_Back);
            titleView = findViewById(R.id.baseListActivity_title);
            customButton = findViewById(R.id.baseListActivity_CustomButton);
            recyclerView = findViewById(R.id.baseListActivity_recyclerView);
        }
    public void initUI(){//初始化UI
        initRecyclerView();
        if(activityId==-10){
            customButton.setVisibility(View.INVISIBLE);//不可见
        }else{
             customButton.setImageResource(customButtonId);
        }
    }
    public void initRecyclerView(){//初始化RecyclerView
     //  IBaseListAdapter adapter;//基础列表适配器
        LinearLayoutManager linearLayoutManager;
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
       // adapter=new listAdapter(this);
        recyclerView.setAdapter(createActivity.bindAdapter());
        createActivity.initItem();
        linearLayoutManager.scrollToPosition(0);
       /* adapter.setOnItemClickListener(new cardViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {//每一个item对应的点击事件
               Intent intent=new Intent(this,bain_marle_activity.class);
                startActivity(intent);
            }
        });
          */
    }
    public RecyclerView getRecyclerView(){
        return  recyclerView;
    }
    @Override
    public void onClick(View v) {
        createActivity.onclick();
    }
    public interface  CreateActivity{//对列表项进行初始化
         void onclick();
         void initItem();
        RecyclerView.Adapter bindAdapter();
    }
    public void setCreateActivity(CreateActivity createActivity) {
        this.createActivity = createActivity;
    }
}
