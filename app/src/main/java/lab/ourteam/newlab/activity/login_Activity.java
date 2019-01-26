package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;

import lab.ourteam.newlab.Constant;
import lab.ourteam.newlab.Presenter.loginPresenter;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.View.loginView;

public class login_Activity extends Activity implements loginView {
    private ImageView returnMenu;
    private Button login_button;
    private Button register_button;
    private Intent intent;
    private loginPresenter mPresenter;
    private EditText username_edit,userpassword_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        intent = getIntent();
        this.mPresenter = new loginPresenter(this);
        initId();
        onListener();
        returnMenu.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
    }
    public void initId(){
        returnMenu=(ImageView)findViewById(R.id.login_return_menu);
        login_button=(Button)findViewById(R.id.login_button);
        register_button=(Button)findViewById(R.id.register_button);
        username_edit=findViewById(R.id.username_edit);
        userpassword_edit=findViewById(R.id.userpassword_edit);}
    public void onListener(){
        View.OnClickListener button_listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.login_button:{
                        mPresenter.login();
                        break;
                    }
                    case R.id.register_button:{
                        Intent intent=new Intent(login_Activity.this,register_Activity.class);
                        startActivityForResult(intent, Constant.login_activity_request_code);
                        break;
                    }
                }
            }
        };
        login_button.setOnClickListener(button_listener);
        register_button.setOnClickListener(button_listener);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==Constant.login_activity_request_code&&resultCode==Constant.register_activity_result_code){
            setResult(Constant.login_activity_result_code, intent);
            finish();
        }
    }

    @Override
    public String getUserPhone(){
        return username_edit.getText().toString();
    }

    @Override
    public String getUserPassword() {
        return userpassword_edit.getText().toString();
    }

    @Override
    public void onLoginSeccess(HashMap map){
       //更新UI
        intent.putExtra("userInfo",(Serializable)map);
        setResult(Constant.login_activity_result_code, intent);
        finish();
       // Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onLoginFails(){
        Toast.makeText(getApplicationContext(), "登陆失败！", Toast.LENGTH_LONG).show();
    }

    @Override
    public <T extends Activity> T getSelfActivity() {
        return null;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
