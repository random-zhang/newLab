package lab.ourteam.newlab.View;

import android.app.Activity;

public interface IView {
    <T extends Activity> T getSelfActivity();
    void showToast(String msg);
}
