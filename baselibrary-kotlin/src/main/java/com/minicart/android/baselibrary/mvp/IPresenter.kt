package com.minicart.android.baselibrary.mvp

/**
 * @类名：IPresenter
 * *
 * @描述：
 * *
 * @创建人：54506
 * *
 * @创建时间：2017/1/13 11:04
 * *
 * @版本：
 */

interface IPresenter<in V> {

    fun injectView(view: V)

    fun onAttachView()

    fun onStart()

    fun onRestart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    companion object {

        val SORT_TYPE = "01"
        val PAGE_SIZE = "2000"
        val CURRENT_PAGE = "1"
    }

}
