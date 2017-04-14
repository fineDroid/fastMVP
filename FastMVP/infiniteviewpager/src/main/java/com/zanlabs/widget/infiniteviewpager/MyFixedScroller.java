package com.zanlabs.widget.infiniteviewpager;

import android.content.Context;
import android.widget.Scroller;

/**
 * Created by huxiaoyuan on 16/3/16.
 */
public class MyFixedScroller extends Scroller {
    public static final float AUTO_DURATION=4.5f;
    public static final float DEFAULT_DURATION=1f;
   private float fixedDuration=DEFAULT_DURATION;
    public MyFixedScroller(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        duration*=fixedDuration;
        super.startScroll(startX, startY, dx, dy, duration);
    }

    public void setFixedDuration(float fixedDuration) {
        this.fixedDuration = fixedDuration;
    }
}
