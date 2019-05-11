package lab.ourteam.newlab.Presenter;

import java.lang.ref.WeakReference;

import lab.ourteam.newlab.Model.IModel;
import lab.ourteam.newlab.View.IView;

public class  IPresenter {
    protected IModel mIModel;
    protected WeakReference<IView> mViewReference;
}
