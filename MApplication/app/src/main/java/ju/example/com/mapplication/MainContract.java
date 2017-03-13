package ju.example.com.mapplication;

import ju.example.com.mapplication.mvp.BasePresenter;
import ju.example.com.mapplication.mvp.BaseView;

public class MainContract {
    interface View extends BaseView {
        void refreshView();
    }

    interface Presenter extends BasePresenter<View> {
        void requestNetWork();
    }

}
