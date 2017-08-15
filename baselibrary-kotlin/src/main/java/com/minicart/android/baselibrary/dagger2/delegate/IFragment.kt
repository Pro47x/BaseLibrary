package com.minicart.android.baselibrary.dagger2.delegate


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

import com.minicart.android.baselibrary.dagger2.component.AppComponent


/**
 * Created by jess on 26/04/2017 21:42
 * Contact with jess.yan.effort@gmail.com
 */

interface IFragment {
    fun setupFragmentComponent(appComponent: AppComponent)

    /**
     * 初始化根布局ID

     * @param savedInstanceState
     * *
     * @return
     */
    fun initRootLayoutID(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): Int

    fun initView(savedInstanceState: Bundle)

    fun initData(savedInstanceState: Bundle)

    fun initListener(savedInstanceState: Bundle)

    fun launchActivity(intent: Intent)

    fun killMyself()
}
