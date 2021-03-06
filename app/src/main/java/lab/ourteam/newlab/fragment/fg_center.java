package lab.ourteam.newlab.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lab.ourteam.newlab.Adapter.myAdapter;
import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Bean.listviewBean;
import lab.ourteam.newlab.Constant;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.activity.login_Activity;
import lab.ourteam.newlab.activity.myDevices;
import lab.ourteam.newlab.activity.myHistory;
import lab.ourteam.newlab.activity.settingListViewActivity;
import lab.ourteam.newlab.activity.userDevices;
import lab.ourteam.newlab.event.MessageEvent;
import lab.ourteam.newlab.activity.user_info_activity;

import static lab.ourteam.newlab.Utils.saveToLocation.getUserInfo;

public class fg_center extends Fragment {
    private ListView listView;
    private View view;
    private List<listviewBean> viewList;
    private myAdapter viewList_adapter;
    private RelativeLayout user_info_team;
    private TextView fg_center_user_name,fg_center_user_id;
    private ImageView userImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fg_center,container,false);
        EventBus.getDefault().register(this);
        initId();
        initListView();
        initListener();
        initUI();
        return view;
    }
    private void initId(){
        listView=view.findViewById(R.id.fg_center_ListView);
        user_info_team=(RelativeLayout)view.findViewById(R.id.fg_center_user_info_team);
        fg_center_user_name=(TextView)view.findViewById(R.id.fg_center_user_name);
        userImage=view.findViewById(R.id.userImage);
        fg_center_user_id=view.findViewById(R.id.fg_center_user_id);
    }
    private void initUI(){
        User user= null;//先从本地查找用户信息
        try {
            user = getUserInfo(getContext());
            if(user==null){//找不到转入登录系统
            }else{//找到呈现在页面上
                fg_center_user_id.setText("ID:"+user.getUserId());
                fg_center_user_name.setText(user.getUserName());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    private void initListView(){
        viewList= new ArrayList<>();
      viewList.add(new listviewBean(R.mipmap.myhistory,"历史记录",101));
      viewList.add(new listviewBean(R.mipmap.mydevice,"我的设备",102));
      viewList.add(new listviewBean(R.mipmap.myvideo,"设置",103));
      viewList_adapter=new myAdapter(getContext(),viewList,R.layout.content_item);
      listView.setAdapter(viewList_adapter);
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            switch(position){
                case 0://历史记
                    startActivity(new Intent(getContext(), myHistory.class));
                    break;
                case 1://我的设备
                   Intent intent=new Intent(getContext(), myDevices.class);
                   startActivity(intent);
                    break;
                case 2://设置
                     startActivity(new Intent(getContext(), settingListViewActivity.class));
                    break;
            }
          }
      });
    }
    /*
     添加点击事件
     */
    private void initListener(){
        user_info_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    try {
                        if(getUserInfo(getContext())==null)//是否打开登录界面
                            intent.setClass(getContext(), login_Activity.class);
                        else {
                            intent.setClass(getContext(), user_info_activity.class);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivityForResult(intent, Constant.fg_center_request_code);//打开用户信息列表
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==Constant.fg_center_request_code&&resultCode== Constant.login_activity_result_code){
            User user=(User)data.getSerializableExtra("userInfo");
            fg_center_user_name.setText(user.getUserName());
            fg_center_user_id.setText("ID:"+user.getUserId());
            //头像设置

        }
        if(requestCode==Constant.fg_center_request_code&&resultCode== Constant.user_info_activity_result_code){
            userImage.setImageURI(Uri.parse(data.getStringExtra("userInfoPortrait")));
        }
    }
    @Subscribe//(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent msg) {
        String portriatPath=(String)msg.getMap().get("portriat");
        if (portriatPath==null)
            return;
        double width= userImage.getWidth();
        double height=userImage.getHeight();
        Bitmap bitmap= BitmapFactory.decodeFile(portriatPath);
        double scalew=width/bitmap.getWidth(),scaleh=height/bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale((float)scalew, (float)scaleh);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
        Log.i("wechat", "压缩后图片的大小" + (bitmap.getByteCount() / 1024 / 1024)
                + "M宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
        userImage.setImageBitmap(bitmap);
      //  mText.setText(messageEvent.getMessage());
    }


}
