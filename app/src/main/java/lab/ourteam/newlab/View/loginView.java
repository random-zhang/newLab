package lab.ourteam.newlab.View;

import android.content.Context;

import java.util.HashMap;

import lab.ourteam.newlab.Bean.User;

public interface loginView extends IView {
    String getUserPhone();
    String getUserPassword();
     void  onLoginSeccess(User user);
    void onLoginFails();
    Context getContext();
}