package ju.example.com.mapplication;

import ju.example.com.mapplication.mvp.BasePresenterImpl;

/**
 * 类名
 *
 * @author zhangchengju
 *         实现的主要功能:
 *         创建日期 2017/3/13
 *         修改者，修改日期，修改内容
 */
public class MainPresenter extends BasePresenterImpl<MainContract.View> implements MainContract.Presenter {

    @Override
    public void attachView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void requestNetWork() {
        mView.refreshView();
    }
}
