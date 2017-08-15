package com.minicart.android.baselibrary.dagger2.config

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager

import com.minicart.android.baselibrary.dagger2.App
import com.minicart.android.baselibrary.dagger2.delegate.AppDelegate
import com.minicart.android.baselibrary.dagger2.module.GlobalConfigModule

/**
 * 此接口可以给框架配置一些参数,需要实现类实现后,并在AndroidManifest中声明该实现类
 * Created by jess on 12/04/2017 11:37
 * Contact with jess.yan.effort@gmail.com
 */

interface ConfigModule {

    /**
     * 使用[GlobalConfigModule.Builder]给框架配置一些配置参数

     * @param context
     * @param builder
     * @param app
     */
    fun applyOptions(context: Context, builder: GlobalConfigModule.Builder, app: App)

    /**
     * 使用[AppDelegate.Lifecycle]在Application的生命周期中注入一些操作

     * @return
     */
    fun injectAppLifecycle(context: Context, lifecycles: List<AppDelegate.Lifecycle>)

    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     * @param context
     * @param lifecycles
     */
    fun injectActivityLifecycle(context: Context, lifecycles: List<Application.ActivityLifecycleCallbacks>)


    /**
     * 使用[FragmentManager.FragmentLifecycleCallbacks]在Fragment的生命周期中注入一些操作
     * @param context
     * @param lifecycles
     */
    fun injectFragmentLifecycle(context: Context, lifecycles: List<FragmentManager.FragmentLifecycleCallbacks>)

    companion object {
        val MODULE_VALUE = "ConfigModule"
    }
}
