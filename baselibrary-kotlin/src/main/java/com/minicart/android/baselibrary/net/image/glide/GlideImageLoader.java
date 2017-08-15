package com.minicart.android.baselibrary.net.image.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.minicart.android.baselibrary.net.image.IImageLoader;

import java.io.File;

/**
 * @类名：GlideUtil
 * @描述：
 * @创建人：54506
 * @创建时间：2017/1/19 15:30
 * @版本：
 */

public class GlideImageLoader implements IImageLoader {

    public GlideImageLoader() {
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void loadNet(ImageView target, String url, Options options) {
        try {
            load(getRequestManager(target.getContext()).load(url), target, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadResource(ImageView target, int resId, Options options) {
        load(getRequestManager(target.getContext()).load(resId), target, options);
    }

    @Override
    public void loadAssets(ImageView target, String assetName, Options options) {
        load(getRequestManager(target.getContext()).load("file:///android_asset/" + assetName), target, options);
    }

    @Override
    public void loadFile(ImageView target, File file, Options options) {
        load(getRequestManager(target.getContext()).load(file), target, options);
    }

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    private RequestManager getRequestManager(Context context) {
        return Glide.with(context);
    }

    private void load(DrawableTypeRequest request, ImageView target, Options options) {
        if (options == null) options = Options.defaultOptions();

        if (options.loadingResId != Options.RES_NONE) {
            request.placeholder(options.loadingResId);
        }
        if (options.loadErrorResId != Options.RES_NONE) {
            request.error(options.loadErrorResId);
        }
        request.crossFade().into(target);
    }
}
