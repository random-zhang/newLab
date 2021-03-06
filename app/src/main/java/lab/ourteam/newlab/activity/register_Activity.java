package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Constant;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.View.registerView;
import lab.ourteam.newlab.myApplication;

import static lab.ourteam.newlab.Utils.postToTomcat.postJson;

public class register_Activity extends Activity  {
    private ImageView return_View;
    private static Button register_send_verification_code;//发送验证码
    private Button register_next_button;//下一步注册
    private EditText register_verification_code_edit;
    private EditText register_userphone_edit;
    private  EventHandler eventHandler;
    private register_AsyncTask send_Task;//请求验证码
    private register_AsyncTask task;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        MobSDK.init(this);
        intent=this.getIntent();
        initId();
        initListener();
    }
    private void initId(){
        register_send_verification_code=(Button)findViewById(R.id.register_send_verification_code);
        register_next_button=(Button)findViewById(R.id.register_next_button);
        register_verification_code_edit=(EditText)findViewById(R.id.register_verification_code_edit);
        register_userphone_edit=(EditText)findViewById(R.id.register_userphone_edit);
        return_View=findViewById(R.id.register_return_menu);
    }
    private void initListener(){

        View.OnClickListener button_listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.register_next_button:{
                        String verification_code=register_verification_code_edit.getText().toString();
                        String phone=register_userphone_edit.getText().toString();
                        //提交验证码
                        SMSSDK.submitVerificationCode("86",phone,verification_code);
                        break;
                    }
                    case R.id.register_send_verification_code:{//发送验证码
                        //在此处判断
                        final String phone=register_userphone_edit.getText().toString();
                        if(phone!=null&&phone.length()==11){
                            if(send_Task!=null )
                                send_Task.cancel(true);
                                send_Task = new register_AsyncTask();
                                send_Task.execute(30);//三十秒后可重新发送
                                register_next_button.setEnabled(true);
                                eventHandler = new EventHandler() {
                                public void afterEvent(int event, int result, Object data) {
                                    // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                                    Message msg = new Message();
                                    msg.arg1 = event;
                                    msg.arg2 = result;
                                    msg.obj = data;
                                    new Handler(Looper.getMainLooper(), new Handler.Callback() {
                                        @Override
                                        public boolean handleMessage(Message msg) {
                                            int event = msg.arg1;
                                            int result = msg.arg2;
                                            Object data = msg.obj;
                                            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                                if (result == SMSSDK.RESULT_COMPLETE) {
                                                    // TODO 处理成功得到验证码的结果
                                                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                                                    register_send_verification_code.setEnabled(false);
                                                    /**
                                                     * 在此，处理按钮信息
                                                     */
                                                } else {
                                                    // TODO 处理错误的结果
                                                    ((Throwable) data).printStackTrace();
                                                }
                                            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                                if (result == SMSSDK.RESULT_COMPLETE) {
                                                    // TODO 处理验证码验证通过的结果
                                                    Toast.makeText(getApplicationContext(),"验证成功",Toast.LENGTH_SHORT).show();
                                                    /**
                                                     * 在此连接数据库判断手机号是否已注册
                                                     */
                                                   task=new register_AsyncTask(Constant.Login_URL,phone,false);
                                                   task.execute(2);
                                                    //new register_AsyncTask(Constant.Login_URL,phone,false).execute(2);
                                                    //设置完成注册按钮可见
                                                   //
                                                } else {
                                                    // TODO 处理错误的结果
                                                    Toast.makeText(getApplicationContext(),"验证错误,请重新输入验证码",Toast.LENGTH_SHORT).show();
                                                    ((Throwable) data).printStackTrace();
                                                }
                                            }
                                            // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                                            return false;
                                        }
                                    }).sendMessage(msg);
                                }
                            };
                            // 注册一个事件回调，用于处理SMSSDK接口请求的结果
                            SMSSDK.registerEventHandler(eventHandler);
                               // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
                            SMSSDK.getVerificationCode("86", phone);
                        }else{
                            Toast.makeText(getApplicationContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case R.id.register_return_menu:{
                        finish();
                        break;
                    }
                }
            }
        };
        register_next_button.setOnClickListener(button_listener);
        register_send_verification_code.setOnClickListener(button_listener);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==Constant.register_activity_request_code&&resultCode==Constant.finish_register_result_code){
            finish();
        }
    }
    // 使用完EventHandler需注销，否则可能出现内存泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    private   class register_AsyncTask extends AsyncTask<Integer, Integer,String >{
            private String Url="user/isRegister.json";
            private String phone;
            private String is_use_password;
            private  int executeCode;//状态码，确定后台执行的是什么操作
            private register_AsyncTask(){
            }
            private register_AsyncTask(String url,String phone,boolean is_use_password){
                this.phone=phone;
                this.is_use_password=String.valueOf(is_use_password);
            }
        @Override
        protected void onPreExecute(){//执行后台耗时操作前被调用,通常用于进行初始化操作.
            super.onPreExecute();
            //time_start=System.currentTimeMillis();
        }
        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行时调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(Integer ...params){ //异步执行后台线程要完成的任务,耗时操作将在此方法中完成.
            executeCode=params[0];
            switch(executeCode) {
                case 30: {//执行倒计时任务
                    try {
                        for (int i = 30; i>0; i--) {
                            publishProgress(i);
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   break;
                }
                case 2:{
                    //查询此号码是否已经注册
                    JSONObject object=new JSONObject();
                    try {
                        object.put("userPhone",phone);
                        String response=postJson(Url,object.toString());
                        if(response!=null){
                            return response;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
            }
        }
            return " ";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            if(executeCode==30)
               register_send_verification_code.setText("请"+values[0].intValue()+"秒后重新发送");
        }
            @Override
            protected void onPostExecute(String s) {//当doInBackground方法完成后,系统将自动调用此方法,
                if(executeCode==30) {
                    register_send_verification_code.setEnabled(true);
                    register_send_verification_code.setText("发送验证码");
                }else if(executeCode==2){
                    try {
                        Log.e("注册测试",s);
                        JSONObject responseObj = new JSONObject(s);
                        int status=responseObj.getInt("status");
                       // String userName=responseObj.getString("userName");
                        if(status==100){//已注册
                            Toast.makeText(getApplicationContext(),"此帐号已注册",Toast.LENGTH_SHORT).show();
                        }else if(status==102){//未注册
                            Intent intent=new Intent(register_Activity.this,finish_register.class);
                            intent.putExtra("phone",phone);//把手机号传到finish_register
                            startActivityForResult(intent,Constant.register_activity_request_code);
                            setResult(Constant.register_activity_result_code, intent);

                    }}catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
