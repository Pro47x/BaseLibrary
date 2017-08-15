package com.minicart.android.baselibrary.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @类名：NoScrollViewPager
 * @描述：一个不可以滑动的ViewPager
 * @创建人：54506
 * @创建时间：2016/8/10 9:47
 * @版本：
 */
public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        this(context, null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //不拦截子控件的事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //触摸时什么都不做
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
