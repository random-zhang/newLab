package lab.ourteam.newlab.View;

import java.util.HashMap;

public interface loginView extends IView {
    String getUserPhone();
    String getUserPassword();
    void onLoginSeccess(HashMap map);
    void onLoginFails();
}