package lab.ourteam.newlab.Presenter;

import java.lang.ref.WeakReference;

import lab.ourteam.newlab.Model.loginModel;
import lab.ourteam.newlab.View.IView;
import lab.ourteam.newlab.View.loginView;

public class loginPresenter extends IPresenter {
    public  loginPresenter(loginView loginView) {
        this.mIModel = new loginModel();
        this.mViewReference = new WeakReference<IView>(loginView);
    }
    public void login() {
        if (mIModel != null && mViewReference != null && mViewReference.get() != null) {
            loginView loginView = (loginView) mViewReference.get();
            String phone = loginView.getUserPhone();
            String passWord = loginView.getUserPassword();

            //此时LoginListener作为匿名内部类是持有外部类的引用的。
            if(((loginModel)mIModel).login(phone,passWord))
                loginView.onLoginSeccess(((loginModel)mIModel).getmUserInfo());
            else
                loginView.onLoginFails();
        }
    }

}