package com.minicart.android.baselibrary.dagger2.delegate


import android.content.Intent
import android.os.Bundle
import com.minicart.android.baselibrary.dagger2.component.AppComponent
import com.minicart.android.baselibrary.mvp.IView


/**
 * Created by jess on 26/04/2017 21:42
 * Contact with jess.yan.effort@gmail.com
 */

interface IActivity<out P> : IView<P> {
    fun setupActivityComponent(appComponent: AppComponent)

    fun useFragment(): Boolean

    /**
     * 初始化根布局ID

     * @param savedInstanceState
     * *
     * @return
     */
    fun initRootLayoutID(savedInstanceState: Bundle): Int

    fun initView(savedInstanceState: Bundle)

    fun initData(savedInstanceState: Bundle)

    fun initListener(savedInstanceState: Bundle)

    override fun launchActivity(intent: Intent)

    override fun killMyself()

}
