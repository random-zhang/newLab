package lab.ourteam.newlab.Presenter;

import java.lang.ref.WeakReference;

import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Model.registerModel;
import lab.ourteam.newlab.View.IView;
import lab.ourteam.newlab.View.registerView;

public class registerPresenter extends IPresenter {

    public  registerPresenter(registerView registerView) {
        this.mIModel = new registerModel();
        this.mViewReference = new WeakReference<IView>(registerView);
    }
    public void register(){
        if (mIModel != null && mViewReference != null && mViewReference.get() != null) {
            registerView view = (registerView) mViewReference.get();
            String phone = view.getUserPhone();//从activity获取而来
            String passWord = view.getUserPassword();
            String name=view.getUserName();
            User user=((registerModel)mIModel).register(phone,name,passWord);
            if(user!=null)
                view.onRegisterSeccess(user);
            else
                view.onRegisterFails();
        }
    }
}
