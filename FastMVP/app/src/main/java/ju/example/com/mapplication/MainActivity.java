package ju.example.com.mapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;
import com.zanlabs.widget.infiniteviewpager.indicator.CirclePageIndicator;
import com.zanlabs.widget.infiniteviewpager.indicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import ju.example.com.mapplication.viewpager.BannerPagerAdapter;


public class MainActivity extends Activity {
    private InfiniteViewPager infiniteViewPager;
    private CirclePageIndicator circlePageIndicator;
    private LinePageIndicator linePageIndicator;
    private BannerPagerAdapter mAdapter;
    private static final int DELAY_AUTO = 3000;
    private static final int MIN_SCROLL_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Glide.with(this).load("https://i.ytimg.com/vi/fjImkK1xAZI/maxresdefault.jpg")
//                .asBitmap().placeholder(R.mipmap.bg_default).error(R.mipmap.bad_default).into(mImageView);
        infiniteViewPager = (InfiniteViewPager) findViewById(R.id.viewpager);
//        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        linePageIndicator = (LinePageIndicator) findViewById(R.id.indicator);
        Context ctx = this.getApplication();
        mAdapter = new BannerPagerAdapter(ctx);
        infiniteViewPager.setAdapter(mAdapter);
        infiniteViewPager.setAutoScrollTime(DELAY_AUTO);
        infiniteViewPager.startAutoScroll();

//        circlePageIndicator.setPageColor(Color.parseColor("#f2f2f2"));
//        circlePageIndicator.setFillColor(Color.parseColor("#4bd4f7"));
//        circlePageIndicator.setRadius(20f);
//        circlePageIndicator.setViewPager(infiniteViewPager);
//        circlePageIndicator.setSnap(true);

        linePageIndicator.setSelectedColor(Color.parseColor("#4bd4f7"));
        linePageIndicator.setUnselectedColor(Color.parseColor("#f2f2f2"));

        //间隔
        linePageIndicator.setGapWidth(30f);
        //宽度
        linePageIndicator.setLineWidth(60f);
        //高度
        linePageIndicator.setStrokeWidth(20f);
        linePageIndicator.setViewPager(infiniteViewPager);

        List<String> pics = new ArrayList<>();
        pics.add("https://i.ytimg.com/vi/fjImkK1xAZI/maxresdefault.jpg");
        pics.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492160732747&di=475f03f5b60b7e60d9bbd81e61f6dd97&imgtype=0&src=http%3A%2F%2Fdingyue.nosdn.127.net%2FzLvZPWX%3DY%3DpRyGTK6WcYfP6GXjxhwYa7bNbzUUl5JqJOQ1489923107030.jpg");
        pics.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492160732747&di=a34dc088ac1a4a5657b0f346e8329c12&imgtype=0&src=http%3A%2F%2Fi.dimg.cc%2Fa7%2F9d%2F5a%2F26%2F52%2F77%2Fd3%2Ff0%2Fcd%2Fb4%2Ffa%2Fb2%2Ff8%2Fbf%2Fa6%2F83.jpg");
        mAdapter.updateSectionItemContentList(pics);
        infiniteViewPager.startAutoScroll();
    }

//    @Override
//    public void refreshView() {
//        Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show();
//    }
}
