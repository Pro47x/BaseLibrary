package com.minicart.android.baselibrary.dagger2.module;

import android.support.annotation.Nullable;

import com.minicart.android.baselibrary.net.GlobalHttpHandler;
import com.minicart.android.baselibrary.net.RequestInterceptor;
import com.minicart.android.baselibrary.net.http.engine.DefaultHttpEngine;
import com.minicart.android.baselibrary.net.http.engine.IHttpEngine;
import com.minicart.android.baselibrary.net.image.IImageLoader;
import com.minicart.android.baselibrary.net.image.glide.GlideImageLoader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jessyan on 2016/3/14.
 */
@Module
public class ClientModule {
    private static final int TIME_OUT = 30;

    @Singleton
    @Provides
    IImageLoader provideImageLoaderStrategy() {//图片加载框架默认使用glide
        return new GlideImageLoader();
    }

    @Singleton
    @Provides
    IHttpEngine provideHttpEngine(Retrofit retrofit) {
        return new DefaultHttpEngine(retrofit);
    }

    /**
     * @param builder
     * @param client
     * @param httpUrl
     * @return
     * @author: jess
     * @date 8/30/16 1:15 PM
     * @description:提供retrofit
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(
            Retrofit.Builder builder,
            OkHttpClient client,
            HttpUrl httpUrl
    ) {
        builder
                .baseUrl(httpUrl)//域名
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client);//设置okhttp
        return builder.build();
    }

    /**
     * 提供OkhttpClient
     *
     * @param builder
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient provideClient(
            OkHttpClient.Builder builder,
            Interceptor intercept,
            @Nullable final GlobalHttpHandler handler
    ) {
        builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);
        if (handler != null) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                }
            });
        }
        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    Interceptor provideInterceptor(RequestInterceptor interceptor) {
        return interceptor;//打印请求信息的拦截器
    }

}
