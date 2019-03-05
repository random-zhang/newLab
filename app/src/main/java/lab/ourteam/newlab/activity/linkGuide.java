package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import lab.ourteam.newlab.R;
import lab.ourteam.newlab.WifiAutoConnectManager;

import static lab.ourteam.newlab.Constant.connectRouter_result_code;
import static lab.ourteam.newlab.Constant.linkGuide_request_code;
import static lab.ourteam.newlab.Constant.linkGuide_result_code;

public class linkGuide extends Activity implements View.OnClickListener{//连接引导基础类
    private Button next;//下一步
    private ImageView imageView,back;//引导图片,返回上一页
    //private Class nextActivity;
    private int imageResourceId;
    linkGuide(int imageResourceId){
        //this.nextActivity=nextActivity;
        this.imageResourceId=imageResourceId;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_guide);
        next=findViewById(R.id.activity_link_next);
        imageView=findViewById(R.id.activity_link_imageView);
        back=findViewById(R.id.activity_link_return_menu);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageView.setImageResource(imageResourceId);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.activity_link_next:{
                //默认页面
                Intent intent=new Intent(this,connectRouter.class);
                startActivityForResult(intent,linkGuide_result_code);
                break;
            }
            case R.id.activity_link_return_menu:{
                finish();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
          if(requestCode==linkGuide_request_code&&resultCode==connectRouter_result_code){

          }
    }

}
