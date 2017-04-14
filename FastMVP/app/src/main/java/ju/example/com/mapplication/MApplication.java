package ju.example.com.mapplication;

import android.app.Application;

/**
 * 类名
 *
 * @author zhangchengju
 *         实现的主要功能:
 *         创建日期 2017/4/14
 *         修改者，修改日期，修改内容
 */
public class MApplication extends Application {

    private static MApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MApplication getInstance() {
        return instance;
    }

}

