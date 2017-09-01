package com.minicart.android.baselibrary.dagger2.component;

import android.app.Application;

import com.google.gson.Gson;
import com.minicart.android.baselibrary.dagger2.delegate.AppDelegate;
import com.minicart.android.baselibrary.dagger2.module.AppModule;
import com.minicart.android.baselibrary.dagger2.module.ClientModule;
import com.minicart.android.baselibrary.dagger2.module.GlobalConfigModule;
import com.minicart.android.baselibrary.net.http.secret.ISecret;
import com.minicart.android.baselibrary.net.image.IImageLoader;
import com.minicart.android.baselibrary.support.AppManager;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {
    Application application();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    IImageLoader imageLoader();

    //    gson
    Gson gson();

    AppManager getAppManager();

    ISecret getSecret();

    HttpUrl getBaseUrl();

    //用来存取一些整个App公用的数据,切勿大量存放大容量数据
//    Map<String, Object> extras();

    OkHttpClient provideClient();

    void inject(AppDelegate delegate);

}
