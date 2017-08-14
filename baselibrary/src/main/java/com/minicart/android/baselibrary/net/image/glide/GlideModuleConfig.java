package com.minicart.android.baselibrary.net.image.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * @类名：GlideModuleConfig
 * @描述：
 * @创建人：54506
 * @创建时间：2016/12/28 15:31
 * @版本：
 */

public class GlideModuleConfig implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 使用默认的内部磁盘缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context));
        //指定内存缓存大小
        builder.setMemoryCache(new LruResourceCache(ConfigConstants.MAX_CACHE_MEMORY_SIZE));
        //全部的内存缓存用来作为图片缓存
        builder.setBitmapPool(new LruBitmapPool(ConfigConstants.MAX_CACHE_MEMORY_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);//图片质量
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
