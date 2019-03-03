package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import lab.ourteam.newlab.R;

import static lab.ourteam.newlab.Constant.linkGuide_result_code;

public class linkGuide extends Activity implements View.OnClickListener{//连接引导基础类
    private Button next;//下一步
    private ImageView imageView,back;//引导图片,返回上一页
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
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.activity_link_next:{

                break;
            }
            /*case R.id.activity_link_imageView:{

                break;
            }*/
            case R.id.activity_link_return_menu:{
                finish();
            }
        }
    }
    public  void setNextActivity(Class cls ){
        Intent intent=new Intent(this,cls);
        startActivityForResult(intent,linkGuide_result_code);
    }
    public void setImageView(int imageResourceId){//用于设置图片
        imageView.setImageResource(imageResourceId);
    }
}
