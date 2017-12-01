package com.minicart.android.baselibrary.dagger2.config;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.minicart.android.baselibrary.dagger2.App;
import com.minicart.android.baselibrary.dagger2.delegate.AppDelegate;
import com.minicart.android.baselibrary.dagger2.module.GlobalConfigModule;

import java.util.List;

/**
 * 此接口可以给框架配置一些参数,需要实现类实现后,并在AndroidManifest中声明该实现类
 * Created by jess on 12/04/2017 11:37
 * Contact with jess.yan.effort@gmail.com
 */

public interface ConfigModule {
    String MODULE_VALUE = "ConfigModule";
    int CONFIG_LOCK_STATUS_BAR = 1 << 0;
    int CONFIG_NO_SUPPORT_SELL_ORDER_TYPE_INPUT_AMOUNT = 1 << 1;

    /**
     * 使用{@link GlobalConfigModule.Builder}给框架配置一些配置参数
     *
     * @param context
     * @param builder
     * @param app
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder, App app);

    /**
     * 使用{@link AppDelegate.Lifecycle}在Application的生命周期中注入一些操作
     *
     * @return
     */
    void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles);

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);


    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}
