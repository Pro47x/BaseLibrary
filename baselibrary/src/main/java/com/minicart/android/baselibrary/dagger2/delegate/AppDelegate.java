package com.minicart.android.baselibrary.dagger2.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.minicart.android.baselibrary.dagger2.ActivityLifecycle;
import com.minicart.android.baselibrary.dagger2.App;
import com.minicart.android.baselibrary.dagger2.component.AppComponent;
import com.minicart.android.baselibrary.dagger2.component.DaggerAppComponent;
import com.minicart.android.baselibrary.dagger2.config.ConfigModule;
import com.minicart.android.baselibrary.dagger2.config.MCManifestParser;
import com.minicart.android.baselibrary.dagger2.module.AppModule;
import com.minicart.android.baselibrary.dagger2.module.ClientModule;
import com.minicart.android.baselibrary.dagger2.module.GlobalConfigModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * AppDelegate可以代理Application的生命周期,在对应的生命周期,执行对应的逻辑,因为Java只能单继承
 * 而我的框架要求Application要继承于BaseApplication
 * 所以当遇到某些三方库需要继承于它的Application的时候,就只有自定义Application继承于三方库的Application
 * 再将BaseApplication的代码复制进去,而现在就不用再复制代码,只用在对应的生命周期调用AppDelegate对应的方法(Application一定要实现APP接口)
 * <p>
 * Created by jess on 24/04/2017 09:44
 * Contact with jess.yan.effort@gmail.com
 */

public class AppDelegate implements App {
    private Application mApplication;
    private AppComponent mAppComponent;
    @Inject
    ActivityLifecycle mActivityLifecycle;

    private final ConfigModule mModule;
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallback;

    public AppDelegate(Application application) {
        this.mApplication = application;
        this.mModule = new MCManifestParser(mApplication).parse(ConfigModule.MODULE_VALUE);
    }

    public void onCreate() {
        onCreate(new ClientModule());
    }

    public void onCreate(ClientModule clientModule) {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication))//提供application
                .clientModule(clientModule)//用于提供okhttp和retrofit的单例
                .globalConfigModule(getGlobalConfigModule(mApplication, mModule, this))//全局配置
                .build();
        mAppComponent.inject(this);

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);
        mComponentCallback = new AppComponentCallbacks(mApplication, mAppComponent);
        mApplication.registerComponentCallbacks(mComponentCallback);
//        mModule.registerComponents(mApplication, mAppComponent.repositoryManager().getHttpEngine());
    }


    public void onTerminate() {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mComponentCallback != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallback);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycles = null;
        this.mComponentCallback = null;
        this.mApplication = null;
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     *
     * @return
     */
    private GlobalConfigModule getGlobalConfigModule(Application context, ConfigModule module, App app) {
        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();
        module.applyOptions(context, builder, app);
        return builder.build();
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }


    public interface Lifecycle {
        void onCreate(Application application);

        void onTerminate(Application application);
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            //内存不足时清理图片请求框架的内存缓存
//            mAppComponent.imageLoader().clear(mApplication, GlideImageConfig
//                    .builder()
//                    .isClearMemory(true)
//                    .build());
        }
    }

}
