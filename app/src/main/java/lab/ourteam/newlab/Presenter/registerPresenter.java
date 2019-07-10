package lab.ourteam.newlab.Presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import asyncTask.registerTask;
import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Model.registerModel;
import lab.ourteam.newlab.View.IView;
import lab.ourteam.newlab.View.loginView;
import lab.ourteam.newlab.View.registerView;

import static lab.ourteam.newlab.Utils.saveToLocation.saveUserInfo;

public class registerPresenter extends IPresenter implements registerTask.Listener {
    private registerView view;
    private AsyncTask task;
    public  registerPresenter(registerView registerView) {
        this.mIModel = new registerModel();
        this.mViewReference = new WeakReference<IView>(registerView);
    }
    public void register(){
        if (mIModel != null && mViewReference != null && mViewReference.get() != null) {
            view = (registerView) mViewReference.get();
            String phone = view.getUserPhone();//从activity获取而来
            String passWord = view.getUserPassword();
            String name=view.getUserName();
            task=new registerTask();
            ((registerTask) task).setListener(this);
            task.execute(phone,name,passWord);
          //  User user=((registerModel)mIModel).register(phone,name,passWord);
          /*  if(user!=null)
                view.onRegisterSeccess(user);
            else
                view.onRegisterFails();*/
        }
    }
    @Override
    public void onSuccess(User user) {

    }
    @Override
    public void onFail() {

    }

    @Override
    public User InBackground(String... params) {
        String phone=params[0];
        String name=params[1];
        String password=params[2];
        Context context=view.getContext();
        User user=((registerModel)mIModel).register(phone,name,password);
        if(user!=null){
            saveUserInfo(user,context);
            Log.d("文件","成功保存用户信息到本地");
        }
        return user;
    }



}
