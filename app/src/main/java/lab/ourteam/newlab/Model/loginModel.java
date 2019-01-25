package lab.ourteam.newlab.Model;

import android.database.Observable;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.HashMap;

import lab.ourteam.newlab.listener.HttpResponseListener;

public class loginModel implements IModel {
    //model 负责数据以及业务逻辑。
    private String mUserPhone = "17551020805";
    private String mPassWord = "123";
    private String mUserName="李晓明";
    private String mUserID="1234";

    public boolean login(String userPhone, String password) {//无库模式

        if (userPhone == null||password==null) {
            return false;
        }
        if (mUserPhone.equals(userPhone) && mPassWord.equals(password)){
           //登录成功从数据库中获取用户数据
            return true;
        } else {
            return false;
        }
    }
    public HashMap getmUserInfo(){
        HashMap map=new HashMap();
        map.put("userName",mUserName);
        map.put("userPassword",mPassWord);
        map.put("userPhone",mUserPhone);
        map.put("userID",mUserID);
        return map;
    }

}
