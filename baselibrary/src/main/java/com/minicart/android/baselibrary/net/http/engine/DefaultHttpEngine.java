package com.minicart.android.baselibrary.net.http.engine;


import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;

/**
 * @类名：MCHttpEngine
 * @描述：
 * @创建人：54506
 * @创建时间：2017/3/22 14:28
 * @版本：
 */
@Singleton
public class DefaultHttpEngine implements IHttpEngine {
    private static final String TAG = "DefaultHttpEngine";

    private final Retrofit mRetrofit;
    private final Map<String, Object> mRetrofitServiceCache = new LinkedHashMap<>();

    @Inject
    public DefaultHttpEngine(Retrofit retrofit) {
        mRetrofit = retrofit;
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
