package com.zanlabs.widget.infiniteviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


/**
 * Created by RxRead on 2015/9/24.
 * https://github.com/waylife/InfiniteViewPager
 */
public class InfiniteViewPager extends ViewPager {
    private final static boolean DEBUG = false;
    private final static String TAG = "InfiniteViewPager";

    public static void log(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }
    private static final long DEFAULT_AUTO_SCROLL_INTERVAL = 5000;//5s
    private static final int MSG_AUTO_SCROLL = 1;
    private static final int MSG_SET_PAGE = 2;
    private Handler mHandler;
    private boolean mAutoScroll;
    private boolean mIsInfinitePagerAdapter;
    private MyFixedScroller scroller;
    private final Object mSync = new Object();
    /**
     * whether view is touched when auto scroll is enable
     */
    private boolean mTouchedWhenAutoScroll;
    private OnPageChangeListener mOnPageChangeListener;
    private long mDelay = DEFAULT_AUTO_SCROLL_INTERVAL;

    public InfiniteViewPager(Context context) {
        this(context, null);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void updateScroller(){
        try {
            scroller = new MyFixedScroller(getContext());
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            // scroller.setFixedDuration(5000);
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    void init() {

        updateScroller();
        setOffscreenPageLimit(1);
        //set listeners
        super.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(FakePositionHelper.getFakeFromReal(InfiniteViewPager.this, position), positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position < FakePositionHelper.getStartPosition(InfiniteViewPager.this) || position > FakePositionHelper.getEndPosition(InfiniteViewPager.this)) {
                    synchronized (mSync) {
                        if (mAutoScroll) {
                            mHandler.removeMessages(MSG_SET_PAGE);
                            Message msg = new Message();
                            msg.what = MSG_SET_PAGE;
                            msg.arg1 = position;
                            mHandler.sendMessageDelayed(msg, 500);
                        }
                    }
                    return;
                } else {
                }
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(FakePositionHelper.getFakeFromReal(InfiniteViewPager.this, position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                String sta = "default";
//                if (ViewPager.SCROLL_STATE_IDLE == state) {
//                    sta = "idle";
//                } else if (ViewPager.SCROLL_STATE_DRAGGING == state) {
//                    sta = "dragging";
//                } else if (ViewPager.SCROLL_STATE_SETTLING == state) {
//                    sta = "setting";
//                }
//                log("onPageScrollStateChanged->" + sta);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        //
        mHandler = new Handler() {
            WeakReference<InfiniteViewPager> myViewPager=new WeakReference<InfiniteViewPager>(InfiniteViewPager.this);
            WeakReference<MyFixedScroller> myScroller =new WeakReference<MyFixedScroller>(scroller);

            @Override
            public void handleMessage(Message msg) {
                log("handleMessage : " + msg.what);
                switch (msg.what) {
                    case MSG_AUTO_SCROLL:
                        if(myScroller.get()!=null){
                            myScroller.get().setFixedDuration(MyFixedScroller.AUTO_DURATION);
                        }
//                        setItemToNext();
                        PagerAdapter adapter = getAdapter();
                        if (adapter == null || adapter.getCount() == 0) {
                            stopAutoScroll();
                            return;
                        }
                        if(myViewPager.get()!=null) {
                            int totalCount = isInfinitePagerAdapter() ? FakePositionHelper.getRealAdapterSize(myViewPager.get()) : adapter.getCount();
                            if (totalCount <= 1)
                                return;

                            int nextItem = getFakeCurrentItem() + 1;
                            if (isInfinitePagerAdapter()) {
                                setFakeCurrentItem(nextItem);
                            } else {
                                if (nextItem == totalCount) {
                                    setFakeCurrentItem(0);
                                }
                            }
                        }
                        if(myScroller.get()!=null){
                            myScroller.get().setFixedDuration(MyFixedScroller.DEFAULT_DURATION);
                        }
                        sendDelayMessage();
                        break;
                    case MSG_SET_PAGE:
                        if(myViewPager.get()!=null){
                            setFakeCurrentItem(FakePositionHelper.getRealPositon(myViewPager.get(), msg.arg1), false);
                        }
                        break;
                }
            }
        };
    }


    public void startAutoScroll() {
        startAutoScroll(this.mDelay);
    }

    public void startAutoScroll(long delayTime) {
        log("startAutoScroll");
        if (getAdapter() == null || getAdapter().getCount() == 0)
            return;
        this.mDelay = delayTime;
        this.mAutoScroll = true;
        mHandler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, mDelay);

    }

    private void sendDelayMessage() {
        log("sendDelayMessage");
        synchronized(mSync) {
            if (mAutoScroll){
                mHandler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, mDelay);
             }
        }
    }


    public void stopAutoScroll() {
        log("stopAutoScroll");
        synchronized(mSync) {
            this.mAutoScroll = false;
            mHandler.removeMessages(MSG_AUTO_SCROLL);
            mHandler.removeMessages(MSG_SET_PAGE);
        }
    }

    public void setAutoScrollTime(long autoScrollTime) {
        this.mDelay = autoScrollTime;
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }


    private void setItemToNext() {
        PagerAdapter adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            stopAutoScroll();
            return;
        }
        int totalCount = isInfinitePagerAdapter() ? FakePositionHelper.getRealAdapterSize(this) : adapter.getCount();
        if (totalCount <= 1)
            return;

        int nextItem = getFakeCurrentItem() + 1;
        if (isInfinitePagerAdapter()) {
            setFakeCurrentItem(nextItem);
        } else {
            if (nextItem == totalCount) {
                setFakeCurrentItem(0);
            }
        }
    }




    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(FakePositionHelper.getRealFromFake(this, item));
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(FakePositionHelper.getRealFromFake(this, item), smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        return FakePositionHelper.getFakeFromReal(this, getFakeCurrentItem());
    }

    private int getFakeCurrentItem() {
        return super.getCurrentItem();
    }


    private void setFakeCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    private void setFakeCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    private int getAdapterSize() {
        return getAdapter() == null ? 0 : getAdapter().getCount();
    }

    private boolean isInfinitePagerAdapter() {
        return mIsInfinitePagerAdapter;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        mIsInfinitePagerAdapter = getAdapter() instanceof InfinitePagerAdapter;
        if (!mIsInfinitePagerAdapter) {
            throw new IllegalArgumentException("Currently, only InfinitePagerAdapter is supported");
        }
        setFakeCurrentItem(FakePositionHelper.getRealPositon(InfiniteViewPager.this, 0), false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //to solve conflict with parent ViewGroup
//        getParent().requestDisallowInterceptTouchEvent(true);
        if (this.mAutoScroll || this.mTouchedWhenAutoScroll) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    this.mTouchedWhenAutoScroll = true;
//                    stopAutoScroll();
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mAutoScroll || this.mTouchedWhenAutoScroll) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    this.mTouchedWhenAutoScroll = false;
//                    startAutoScroll();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    public static class FakePositionHelper {
        /**
         * Can not be less than 3
         */
        public final static int MULTIPLIER = 5;

        public static int getRealFromFake(InfiniteViewPager viewPager, int fake) {
            int realAdapterSize = viewPager.getAdapterSize() / MULTIPLIER;
            if (realAdapterSize == 0)
                return 0;
            fake = fake % realAdapterSize;//ensure it
            int currentReal = viewPager.getFakeCurrentItem();
            int real = fake + (currentReal - currentReal % realAdapterSize);//set to the target level
            return real;
        }

        public static int getFakeFromReal(InfiniteViewPager viewPager, int real) {
            int realAdapterSize = viewPager.getAdapterSize() / MULTIPLIER;
            if (realAdapterSize == 0)
                return 0;
            return real % realAdapterSize;
        }

        public static int getStartPosition(InfiniteViewPager viewPager) {
            int realAdapterSize = viewPager.getAdapterSize() / MULTIPLIER;
            return realAdapterSize;
        }

        public static int getEndPosition(InfiniteViewPager viewPager) {
            int realAdapterSize = viewPager.getAdapterSize() / MULTIPLIER;
            return realAdapterSize * (MULTIPLIER - 1) - 1;
        }

        public static int getRealAdapterSize(InfiniteViewPager viewPager) {
            return viewPager.isInfinitePagerAdapter() ? viewPager.getAdapterSize() / MULTIPLIER : viewPager.getAdapterSize();
        }

        public static int getAdapterSize(ViewPager viewPager) {
            if (viewPager instanceof InfiniteViewPager) {
                InfiniteViewPager infiniteViewPager = (InfiniteViewPager) viewPager;
                return getRealAdapterSize(infiniteViewPager);
            }
            PagerAdapter adapter = viewPager.getAdapter();
            if (adapter instanceof InfinitePagerAdapter) {
                InfinitePagerAdapter infinitePagerAdapter = (InfinitePagerAdapter) viewPager.getAdapter();
                return infinitePagerAdapter.getItemCount();
            }
            return adapter == null ? 0 : adapter.getCount();
        }

        public static int getRealPositon(InfiniteViewPager viewPager, int position) {
            int realAdapterSize = getRealAdapterSize(viewPager);
            if (realAdapterSize == 0)
                return 0;
            int startPostion = getStartPosition(viewPager);
            int endPosition = getEndPosition(viewPager);
            if (position < startPostion) {
                return endPosition + 1 - realAdapterSize + position % realAdapterSize;
            }
            if (position > endPosition) {
                return startPostion + position % realAdapterSize;
            }
            return position;
        }

        public static boolean isOutOfRange(InfiniteViewPager viewPager, int position) {
            return position < getStartPosition(viewPager) || position > getEndPosition(viewPager);
        }

    }
}
