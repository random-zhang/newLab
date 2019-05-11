package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Constant;
import lab.ourteam.newlab.MyAsyncTask;
import lab.ourteam.newlab.Presenter.loginPresenter;
import lab.ourteam.newlab.Presenter.registerPresenter;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.View.loginView;
import lab.ourteam.newlab.View.registerView;
import lab.ourteam.newlab.myApplication;

import static lab.ourteam.newlab.Utils.saveToLocation.saveUserInfo;

public class finish_register extends Activity implements registerView {
    private EditText register_username_edit;
    private EditText register_password_edit;
    private Button register_finish_button;
    private Intent intent;
    private registerPresenter mPresenter;
    private String phone;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_register);
        intent=this.getIntent();
        this.mPresenter = new registerPresenter(this);
        phone=intent.getStringExtra("phone");
        initId();
        register_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.register();
                //finish_Register();
               /* if(register_password_edit!=null&register_username_edit!=null) {

                    setResult(Constant.finish_register_result_code, intent);
                    finish();
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                }
                */
            }
        });
    }
    private void initId(){
        register_username_edit=(EditText)findViewById(R.id.register_username_edit);
        register_password_edit=(EditText)findViewById(R.id.register_password_edit);
        register_finish_button=(Button)findViewById(R.id.register_finish_button);
    }

    private void finish_Register(){//点击按钮完成注册
         //
        /*String username=register_username_edit.getText().toString();
        String password=register_password_edit.getText().toString();
        String registerUrlStr = Constant.Register_URL+ "?account="+username+"&phone="+phone+ "&password=" + password;
        new MyAsyncTask(registerUrlStr,getApplicationContext()).execute(1);
        myApplication myapplication=new myApplication();
        myapplication.setUserName(username);*/

    }

    @Override
    public String getUserPhone() {
        return phone;
    }

    @Override
    public String getUserName() {
        return register_username_edit.getText().toString();
    }

    @Override
    public String getUserPassword() {
        return register_password_edit.getText().toString();
    }

    @Override
    public void onRegisterSeccess(User user) {//刚成功注册时,把注册后的信息保存在本地
             saveUserInfo(user,this);
    }

    @Override
    public void onRegisterFails() {
       Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT);
    }

    @Override
    public <T extends Activity> T getSelfActivity() {
        return null;
    }

    @Override
    public void showToast(String msg) {

    }
}
