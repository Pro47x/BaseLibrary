package com.minicart.android.baselibrary.base

import android.content.Context
import com.minicart.android.baselibrary.mvp.IModel
import com.minicart.android.baselibrary.mvp.IPresenter
import com.minicart.android.baselibrary.mvp.IView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Pro47x on 2017/9/11.
 */
abstract class BasePresenter<V : IView<*>, M : IModel>(model: M) : IPresenter<V> {
    private val TAG: String = javaClass.name

    private var mView: V? = null

    private val mModel: M = model

    private var mCompositeDisposable: CompositeDisposable? = null

    private var mRxBusDisposable: CompositeDisposable? = null

    override fun injectView(view: V) {
        mView = view
        onAttachView()
    }

    override fun onAttachView() {

    }

    fun getView(): V? = mView

    fun getModel(): M = mModel

    fun getContext(): Context? = getView()?.getContext()

    override fun onStart() {
    }

    override fun onRestart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
        var temp: CompositeDisposable? = mCompositeDisposable
        if (temp != null) {
            clearDisposable(temp)
        }
        temp = mRxBusDisposable
        if (temp != null) {
            clearDisposable(temp)
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = CompositeDisposable()
        }
        this.mCompositeDisposable!!.add(disposable)
    }

    protected fun registerRxBus(disposable: Disposable) {
        if (mRxBusDisposable == null) {
            mRxBusDisposable = CompositeDisposable()
        }
        mRxBusDisposable!!.add(disposable)
    }

    private fun clearDisposable(disposable: CompositeDisposable) {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
    }
}