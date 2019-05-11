package lab.ourteam.newlab.Presenter;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.lang.ref.WeakReference;

import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Model.IModel;
import lab.ourteam.newlab.Model.loginModel;
import lab.ourteam.newlab.Utils.loginAsyncTask;
import lab.ourteam.newlab.View.loginView;
public class loginPresenter  implements loginAsyncTask.Listener {
    private loginAsyncTask myAsyncTask;
    protected IModel mIModel;
    protected WeakReference<loginView> mViewReference;
    public  loginPresenter(loginView loginView) {
        this.mIModel = new loginModel();
        this.mViewReference = new WeakReference<loginView>(loginView);
    }
    public void login() {
        if (mIModel != null && mViewReference != null && mViewReference.get() != null) {
            loginView loginView = (loginView) mViewReference.get();
            String phone = loginView.getUserPhone();
            String passWord = loginView.getUserPassword();
            myAsyncTask=new loginAsyncTask();
            myAsyncTask.setListener(this);
            myAsyncTask.execute(phone,passWord);
        }
    }

    @Override
    public void onSuccess(User user) {
        mViewReference.get().onLoginSeccess(user);
    }
    @Override
    public void onFail() {
        mViewReference.get().onLoginFails();
    }
    @Override
    public User InBackground(String... params) {
        String userPhone=params[0];
        String userPassword=params[1];
        Context context=mViewReference.get().getSelfActivity().getApplicationContext();
        User user=null;
        try {
            user=((loginModel)mIModel).login(userPhone,userPassword,context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}