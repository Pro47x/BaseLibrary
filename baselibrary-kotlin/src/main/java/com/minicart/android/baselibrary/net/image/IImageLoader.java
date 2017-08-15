package com.minicart.android.baselibrary.net.image;

import android.content.Context;
import android.widget.ImageView;

import com.minicart.android.baselibrary.R;

import java.io.File;

/**
 * @类名：ILoader
 * @描述：
 * @创建人：54506
 * @创建时间：2017/2/27 14:56
 * @版本：
 */

public interface IImageLoader {

    void init(Context context);

    void loadNet(ImageView target, String url, Options options);

    void loadResource(ImageView target, int resId, Options options);

    void loadAssets(ImageView target, String assetName, Options options);

    void loadFile(ImageView target, File file, Options options);

    void clearMemoryCache(Context context);

    void clearDiskCache(Context context);

    class Options {

        public static final int RES_NONE = -1;
        public int loadingResId = RES_NONE;//加载中的资源id
        public int loadErrorResId = RES_NONE;//加载失败的资源id

        public static Options defaultOptions() {
            return new Options(R.drawable.loading, R.drawable.loading);
        }

        public Options(int loadingResId, int loadErrorResId) {
            this.loadingResId = loadingResId;
            this.loadErrorResId = loadErrorResId;
        }
    }
}
