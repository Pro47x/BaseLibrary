package com.minicart.android.baselibrary.mvp

import android.content.Context
import android.content.Intent

/**
 * @类名：IView
 * *
 * @描述：
 * *
 * @创建人：54506
 * *
 * @创建时间：2017/1/13 11:04
 * *
 * @版本：
 */

interface IView<out P> {

    fun getPresenter(): P?

    fun getContext(): Context

    fun showLoadingMessage(message: String)

    fun updateLoadingMessage(msg: String)

    fun hideLoading()

    fun hideLoading(delayedMillisecond: Long)

    fun launchActivity(intent: Intent)

    fun killMyself()
}
