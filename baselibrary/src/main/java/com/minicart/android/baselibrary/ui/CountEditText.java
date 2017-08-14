package com.minicart.android.baselibrary.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @类名：CountEditTextView
 * @描述：
 * @创建人：54506
 * @创建时间：2016/8/24 16:55
 * @版本：
 */
public class CountEditText extends android.support.v7.widget.AppCompatEditText {
    private int mCount;
    private int mMaxCount = Integer.MAX_VALUE;
    private int mMinCount = 0;


    public CountEditText(Context context) {
        this(context, null);
    }

    public CountEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 设置数量
     *
     * @param count
     */
    public void setCount(int count) {
        mCount = count;
        setText(String.valueOf(mCount));
    }

    /**
     * 设置数量
     *
     * @param count
     */
    public void setCount(String count) {
        mCount = Integer.parseInt(count);
        setCount(mCount);
    }

    public int getintCount() {
        return mCount;
    }

    public String getStringCount() {
        return String.valueOf(mCount);
    }

    public void addCount() {
        if (mCount >= mMaxCount) {
            return;
        }
        mCount++;
        setText(String.valueOf(mCount));
    }

    public void decreaseCount() {
        if (mCount <= mMinCount) {
            return;
        }
        mCount--;
        setText(String.valueOf(mCount));
    }

    public void setMaxCount(int maxCount) {
        if (maxCount < mMinCount) {
            throw new IllegalArgumentException("传入的最大值不能小于最小值");
        }
        mMaxCount = maxCount;
    }

    public void setMinCount(int minCount) {
        if (minCount > mMaxCount) {
            throw new IllegalArgumentException("传入的最小值不能大于最大值");
        }
        mMinCount = minCount;
    }
}
