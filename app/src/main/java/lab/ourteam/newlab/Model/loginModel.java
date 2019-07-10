package lab.ourteam.newlab.Model;



import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import lab.ourteam.newlab.Bean.User;

import lab.ourteam.newlab.Utils.postToTomcat;
import okhttp3.Response;

public class loginModel implements IModel {
    //model 负责数据以及业务逻辑。
   private User user=null;
    public User login(final  String userPhone, final String password, Context context)throws IOException {
        User user=new User();
        //获取传进来的参数
        JSONObject o=null ;
        if (userPhone == null||password==null) {
            return null;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userPhone",userPhone);  //参数put到json串里
        jsonObject.put("userPassword",password);
        Log.i("登录",jsonObject.toJSONString());
        Response response= null;
        String json=null;
        try {
            response = postToTomcat.postJson("user/login.json",jsonObject.toJSONString());
            json=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (json!= null)
        {
            o = JSONObject.parseObject(json);
            if (o.getIntValue("status")==0)//登录失败
            {
                return null ;
            }
            /*
               登录成功，获取基础信息
             */
            JSONObject e = o.getJSONObject("user");
            if(e==null) return null;
            user=JSONObject.parseObject(json, new TypeReference<User>() {});
            Log.i("登录",user.getUserName());
        }
          return user;
    }
}
