package com.minicart.android.baselibrary.net.http.engine;


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
public class DefaultHttpEngine<Service> implements IHttpEngine<Service> {
    private static final String TAG = "DefaultHttpEngine";
    private final Service mService;

    public DefaultHttpEngine(OkHttpClient client, HttpUrl baseUrl, Class<Service> clazz) {
        mService = new Retrofit.Builder()
                .baseUrl(baseUrl)//域名
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(clazz);
    }

    @Override
    public Service getService() {
        return mService;
    }
}
