package me.ryan.banner.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class Banner extends FrameLayout implements DefaultLifecycleObserver {

    /**
     * 自动滚动延迟
     */
    private static final int AUTO_SCROLL_TIME_SECOND = 2 * 1000;
    /**
     * 核心实现 viewPager2
     */
    private ViewPager2 viewPager;
    /**
     * viewPager2 adapter
     */
    private BannerAdapter adapter;
    /**
     * 自动滚动是否开启
     */
    private boolean autoScrollStart = false;

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int index = getCurrentIndex();
            int count = getCount();
            viewPager.setCurrentItem((index + 1) % count);
            postDelayed(this, AUTO_SCROLL_TIME_SECOND);
        }
    };

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager2.SCROLL_STATE_IDLE && getCurrentIndex() == getCount() - 1) {
                viewPager.setCurrentItem(0, false);
            }
        }
    };

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        initViewPager(context);
    }

    private void initViewPager(Context context) {
        viewPager = new ViewPager2(context);
        adapter = new BannerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.registerOnPageChangeCallback(onPageChangeCallback);
        this.addView(viewPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public int getCurrentIndex() {
        return viewPager.getCurrentItem();
    }

    public int getCount() {
        if (viewPager.getAdapter() != null) {
            return viewPager.getAdapter().getItemCount();
        }
        return 0;
    }

    public void setData(List<String> data) {
        adapter.setData(data);
        startAutoScroll();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
                startAutoScroll();
                break;
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void startAutoScroll() {
        if (autoScrollStart) return;
        autoScrollStart = true;
        postDelayed(autoScrollRunnable, AUTO_SCROLL_TIME_SECOND);
    }

    private void stopAutoScroll() {
        autoScrollStart = false;
        viewPager.removeCallbacks(autoScrollRunnable);
    }

    public void addLifecycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        startAutoScroll();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        stopAutoScroll();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        stopAutoScroll();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        clear();
    }

    public void clear() {
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
        stopAutoScroll();
    }
}
