package com.minicart.android.baselibrary.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 54506 on 2016/5/12.
 * 规定了高度为所有item高度的和
 */
public class MCListView extends ListView {
    public MCListView(Context context) {
        this(context, null);
    }

    public MCListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MCListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
