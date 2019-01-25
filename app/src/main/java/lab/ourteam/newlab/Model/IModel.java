package lab.ourteam.newlab.Model;

import android.database.Observable;
import android.support.annotation.NonNull;

import lab.ourteam.newlab.listener.HttpResponseListener;

public interface IModel {
    /**
     * 发送网络请求
     *
     * @param observable
     * @param callback
     * @param <T>
     */
   // void sendRequest(@NonNull Observable observable, HttpResponseListener callback) ;

    /**
     * 发送网络请求
     *
     * @param tag
     * @param observable
     * @param callback
     * @param <T>
     */
   //  void sendRequest(@NonNull Object tag, @NonNull Observable observable, HttpResponseListener callback) ;

    /**
     * 发送网络请求
     *
     * @param observable 被观察者
     * @param observer   观察者
     * @param <T>
     */
    // void sendRequest(@NonNull Observableobservable, @NonNull HttpObserver observer) ;

    /**
     * 发送网络请求
     *
     * @param tag        请求标记
     * @param observable 被观察者
     * @param observer   观察者
     * @param <T>
     */
    // void sendRequest(@NonNull Object tag, @NonNull Observable observable, @NonNull HttpObserver observer);
}
