package com.minicart.android.baselibrary.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**
 * @author 54506
 * @version 1
 * @createTime：2017/5/4 10:15
 */

public class MyImageButton extends ImageButton {

    private int width = -1;

    private int height = -1;

    private Bitmap bitmap;


    public MyImageButton(Context context) {
        super(context);
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        Drawable drawable = getBackground();
        if (drawable == null) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (width == -1 || height == -1) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            width = getWidth();
            height = getHeight();
        }

        if (null == bitmap || x < 0 || y < 0 || x >= width || y >= height) {
            return false;
        }
        int pixel = bitmap.getPixel(x, y);
        if (Color.TRANSPARENT == pixel) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}
