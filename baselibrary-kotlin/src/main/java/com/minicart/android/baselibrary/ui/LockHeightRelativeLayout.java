package com.minicart.android.baselibrary.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;


/**
 * @author 54506
 * @version 1
 * @createTime：2017/6/12 15:10
 */

public class LockHeightRelativeLayout extends RelativeLayout {
    public LockHeightRelativeLayout(Context context) {
        super(context);
    }

    public LockHeightRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockHeightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int pix = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, Resources.getSystem().getDisplayMetrics());
        height = Math.min(pix, height);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }
}
