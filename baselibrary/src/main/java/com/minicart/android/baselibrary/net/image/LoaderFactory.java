package com.minicart.android.baselibrary.net.image;


import com.minicart.android.baselibrary.net.image.glide.GlideImageLoader;

/**
 * @类名：LoaderFactory
 * @描述：
 * @创建人：54506
 * @创建时间：2017/2/27 15:05
 * @版本：
 */

public class LoaderFactory {
    /**
     * 默认的图片加载器glide
     */
    private static IImageLoader defaultLoader;
    private static IImageLoader customLoader;

    public static IImageLoader getLoader() {
        if (customLoader == null) {
            if (defaultLoader == null) {
                synchronized (LoaderFactory.class) {
                    if (defaultLoader == null) {
                        defaultLoader = new GlideImageLoader();
                    }
                }
            }
            return defaultLoader;
        }
        return customLoader;
    }

    /**
     * 初始化自定义的加载器
     *
     * @param loader
     */
    public static void initLoader(IImageLoader loader) {
        customLoader = loader;
    }
}
