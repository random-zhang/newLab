package lab.ourteam.newlab.Bean;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import lab.ourteam.newlab.Utils.postToTomcat;

public class User implements Serializable{
    private String userPhone;

    private String userName;

    private String userPassword;

    private Integer userId;
    private Bitmap userPortrait;//图片在本地存放的地址
    private String userSex;
    private String userAddress;
    private Date  birthday;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Bitmap getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(Bitmap userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}