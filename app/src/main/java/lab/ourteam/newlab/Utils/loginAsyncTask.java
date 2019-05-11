package lab.ourteam.newlab.Utils;

import android.os.AsyncTask;

import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Model.loginModel;
import lab.ourteam.newlab.View.loginView;

public  class loginAsyncTask extends AsyncTask<String,Void,User> {//在此进行网络处理
    //创建接口，成功时候回调
    private Listener listener;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //在doInBackground方法中进行异步任务的处理.
    @Override
    protected User doInBackground(String... params) {
        return listener.InBackground(params);

    }
    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if(user!=null)
            listener.onSuccess(user);
        else
            listener.onFail();

    }
    public interface Listener {
        void onSuccess(User user);
        void onFail();
        User InBackground(String... params);
    }
    public void setListener(Listener listener) {
        this.listener =listener;
    }
}
