package com.minicart.android.baselibrary.dagger2.module

import com.minicart.android.baselibrary.net.GlobalHttpHandler
import com.minicart.android.baselibrary.net.RequestInterceptor
import com.minicart.android.baselibrary.net.http.engine.DefaultHttpEngine
import com.minicart.android.baselibrary.net.http.engine.IHttpEngine
import com.minicart.android.baselibrary.net.image.IImageLoader
import com.minicart.android.baselibrary.net.image.glide.GlideImageLoader
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by jessyan on 2016/3/14.
 */
@Module
class ClientModule {

    @Singleton
    @Provides
    internal fun provideImageLoaderStrategy(): IImageLoader {//图片加载框架默认使用glide
        return GlideImageLoader()
    }

    @Singleton
    @Provides
    internal fun provideHttpEngine(retrofit: Retrofit): IHttpEngine {
        return DefaultHttpEngine(retrofit)
    }

    /**
     * @param builder
     * *
     * @param client
     * *
     * @param httpUrl
     * *
     * @return
     * *
     * @author: jess
     * *
     * @date 8/30/16 1:15 PM
     * *
     * @description:提供retrofit
     */
    @Singleton
    @Provides
    internal fun provideRetrofit(
            builder: Retrofit.Builder,
            client: OkHttpClient,
            httpUrl: HttpUrl
    ): Retrofit {
        builder
                .baseUrl(httpUrl)//域名
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)//设置okhttp
        return builder.build()
    }

    /**
     * 提供OkhttpClient

     * @param builder
     * *
     * @return
     */
    @Singleton
    @Provides
    internal fun provideClient(
            builder: OkHttpClient.Builder,
            intercept: Interceptor,
            handler: GlobalHttpHandler?
    ): OkHttpClient {
        builder
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept)
        if (handler != null) {
            builder.addInterceptor { chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())) }
        }
        return builder.build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    internal fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @Singleton
    @Provides
    internal fun provideInterceptor(interceptor: RequestInterceptor): Interceptor {
        return interceptor//打印请求信息的拦截器
    }

    companion object {
        private val TIME_OUT = 30
    }

}
