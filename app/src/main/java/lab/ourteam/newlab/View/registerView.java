package lab.ourteam.newlab.View;

import android.content.Context;

import lab.ourteam.newlab.Bean.User;

public interface registerView extends IView {
    String getUserPhone();
    String getUserName();
    String getUserPassword();
    void onRegisterSeccess(User user);
    void onRegisterFails();
    Context getContext();
}
