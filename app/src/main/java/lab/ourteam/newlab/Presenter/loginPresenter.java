package lab.ourteam.newlab.Presenter;

import android.content.Context;
import android.util.Log;

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
    private loginView view;
    public  loginPresenter(loginView loginView) {
        this.mIModel = new loginModel();
        this.mViewReference = new WeakReference<loginView>(loginView);
    }
    public void login() {
        if (mIModel != null && mViewReference != null && mViewReference.get() != null) {
            view = (loginView) mViewReference.get();
            String phone = view.getUserPhone();
            String passWord =view.getUserPassword();
            myAsyncTask=new loginAsyncTask();
            myAsyncTask.setListener(this);
            myAsyncTask.execute(phone,passWord);
        }
    }

    @Override
    public void onSuccess(User user) {
        view.onLoginSeccess(user);
    }
    @Override
    public void onFail() {
        view.onLoginFails();
    }
    @Override
    public User InBackground(String... params) {
        String userPhone=params[0];
        String userPassword=params[1];
        Context context=view.getContext();
        User user=null;
        try {
            user=((loginModel)mIModel).login(userPhone,userPassword,context.getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}