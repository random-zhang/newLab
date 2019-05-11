package lab.ourteam.newlab.Bean;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;

import lab.ourteam.newlab.Utils.postToTomcat;

public class User implements Serializable{
    private String userphone;

    private String username;

    private String userpassword;

    private Integer userid;
    private Bitmap userportrait;//图片在本地存放的地址

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone == null ? null : userphone.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword == null ? null : userpassword.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Bitmap getUserportrait() {
        return userportrait;
    }

    public void setUserportrait(Context context) throws IOException{
           this.userportrait=postToTomcat.getUserPortrait(context);
    }
}