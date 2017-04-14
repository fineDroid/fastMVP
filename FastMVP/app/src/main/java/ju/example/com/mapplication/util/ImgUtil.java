package ju.example.com.mapplication.util;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ju.example.com.mapplication.MApplication;

/**
 * 图片加载工具
 */
public class ImgUtil {

    @SuppressLint("StaticFieldLeak")
    private static Picasso sPicasso;

    private static Picasso getPicasso() {
        if (null == sPicasso) {
            synchronized (ImgUtil.class) {
                if (null == sPicasso) {
                    sPicasso = new Picasso.Builder(MApplication.getInstance()).build();
                }
            }
        }

        return sPicasso;
    }

    /**
     * 退出程序时，shutdown Picasso的实例，优化cpu与内存占用
     */
    public static void destroy() {
        synchronized (ImgUtil.class) {
            if (null != sPicasso) {
                try {
                    sPicasso.shutdown();
                } catch (Exception e) {
                }
                sPicasso = null;
            }
        }
    }

    public static void loadWithNoHolder(String url, ImageView view) {
        getPicasso().load(url).into(view, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("dsfdsf", "dfdfd");
            }

            @Override
            public void onError() {
                Log.d("dsfdsf", "dfdfd");
            }
        });
    }


}
