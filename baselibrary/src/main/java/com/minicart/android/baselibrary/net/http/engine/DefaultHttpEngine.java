package com.minicart.android.baselibrary.net.http.engine;


import com.minicart.android.baselibrary.support.EmptyUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 类名：DefaultHttpEngine
 * 描述：默认的网络引擎
 * 创建人：54506
 * 创建时间：2017/3/22 14:28
 * 版本：
 */
@Singleton
public class DefaultHttpEngine implements IHttpEngine{
    private static final String TAG = "DefaultHttpEngine";
    private final Retrofit mRetrofit;
    private final Map<String, Object> mRetrofitServiceCache = new LinkedHashMap<>();

    @Inject
    public DefaultHttpEngine(OkHttpClient client, HttpUrl baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)//域名
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Override
    public void injectRetrofitService(Class<?>... services) {
        for (Class<?> service : services) {
            if (mRetrofitServiceCache.containsKey(service.getName())) {
                continue;
            }
            mRetrofitServiceCache.put(service.getName(), mRetrofit.create(service));
        }
    }

    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        if (mRetrofitServiceCache.containsKey(service.getName())) {
            return (T) mRetrofitServiceCache.get(service.getName());
        } else {
            injectRetrofitService(service);
            return obtainRetrofitService(service);
        }
    }
}
