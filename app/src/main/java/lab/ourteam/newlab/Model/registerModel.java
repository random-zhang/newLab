package lab.ourteam.newlab.Model;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Utils.httpUtils;

public class registerModel implements IModel {
    public User register(String userPhone,String userName, String password) {
        User user=null;
        if (userPhone == null||password==null||userName==null) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("userPhone",userPhone);  //参数put到json串里
            jsonObject.put("userPassword",password);
            jsonObject.put("userName",userName);
            try {
                HttpResponse response=httpUtils.getResponse("register.json",jsonObject);
                HttpEntity entity = response.getEntity();
                if (entity != null)
                {
                    JSONObject o = JSONObject.parseObject( EntityUtils.toString(entity));
                    switch (o.getIntValue("status")){
                        case 100: {//已注册
                            o.get("msg");
                            break;
                        }
                        case 101:{//注册失败
                            o.get("msg");
                            break;
                        }
                        default:{//注册成功
                            user = new User();
                            o.get("msg");
                            user.setUserId(o.getIntValue("status"));
                            user.setUserName(userName);
                            user.setUserPassword(password);
                            user.setUserPhone(userPhone);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }
          return user;
    }
}
