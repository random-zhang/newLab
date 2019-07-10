package asyncTask;

import android.os.AsyncTask;

import lab.ourteam.newlab.Bean.User;
import lab.ourteam.newlab.Presenter.registerPresenter;

public class registerTask extends AsyncTask<String,Void, User> {
    //创建接口，成功时候回调
    private Listener listener;
    @Override
    protected User doInBackground(String... strings) {
        return listener.InBackground(strings);
    }
    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if(user!=null)
            listener.onSuccess(user);
        else
            listener.onFail();
    }
    public void setListener(Listener listener) {
        this.listener =listener;
    }
    public interface Listener {
        void onSuccess(User user);
        void onFail();
        User InBackground(String... params);
    }
}