package com.minicart.android.baselibrary.base

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minicart.android.baselibrary.dagger2.component.AppComponent
import com.minicart.android.baselibrary.dagger2.delegate.IFragment
import com.minicart.android.baselibrary.mvp.IPresenter
import com.minicart.android.baselibrary.mvp.IView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @类名：BaseFragment
 * *
 * @描述：Fragment的基类
 * *
 * @创建人：54506
 * *
 * @创建时间：2016/8/5 17:47
 * *
 * @版本：
 */
abstract class BaseFragment<P : IPresenter<*>> : Fragment(), IFragment, IView<P> {
    protected val TAG = javaClass.simpleName

    protected var inflater: LayoutInflater? = null
    protected var parent: ViewGroup? = null

    @Inject
    protected var mPresenter: P? = null
    private var compositeDisposable: CompositeDisposable? = null

    protected var mRootView: View? = null

    private val mHandler = BaseActivity.AtyHandler()
    protected var mLoadDialog: ProgressDialog? = null

    init {
        //必须确保在Fragment实例化时setArguments()
        if (arguments == null) {
            arguments = Bundle()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //初始化布局
        this.inflater = inflater
        parent = container
        val layoutID = initRootLayoutID(inflater!!, container!!, savedInstanceState!!)
        if (layoutID != 0) {
            mRootView = inflater.inflate(layoutID, container, false)
        }
        return mRootView
    }

    override fun onStart() {
        super.onStart()
        if (getPresenter() != null) {
            getPresenter()!!.onStart()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle) {

    }

    override fun initData(savedInstanceState: Bundle) {

    }

    override fun initListener(savedInstanceState: Bundle) {

    }

    override fun onResume() {
        super.onResume()
        if (getPresenter() != null) {
            getPresenter()!!.onResume()
        }
    }

    override fun onPause() {
        if (getPresenter() != null) {
            getPresenter()!!.onPause()
        }
        super.onPause()
    }

    override fun onStop() {
        if (getPresenter() != null) {
            getPresenter()!!.onStop()
        }
        super.onStop()
    }

    fun onBackPressed() {

    }

    override fun onDestroyView() {
        if (this.compositeDisposable != null) {
            if (!this.compositeDisposable!!.isDisposed) {
                this.compositeDisposable!!.clear()
            }
            this.compositeDisposable = null
        }
        if (mPresenter != null) {
            mPresenter!!.onDestroy()
            mPresenter = null
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected fun makeView(rsId: Int): View? {
        return inflater?.inflate(rsId, parent, false)
    }

    override fun hideLoading(delayedMillisecond: Long) {
        if (mLoadDialog != null) {
            val message = mHandler.obtainMessage(BaseActivity.WHAT_CLOSE_PROGRESS_DIALOG, mLoadDialog)
            mHandler.sendMessageDelayed(message, delayedMillisecond)
        }
    }

    override fun hideLoading() {
        hideLoading(0)
    }

    override fun showLoadingMessage(message: String) {
        if (mLoadDialog == null) {
            mLoadDialog = ProgressDialog(context)
        }
        mLoadDialog!!.setMessage(message)
        mLoadDialog!!.show()
    }

    override fun updateLoadingMessage(msg: String) {
        if (mLoadDialog == null) {
            return
        }
        if (mLoadDialog!!.isShowing) {
            mLoadDialog!!.setMessage(msg)
        }
    }

    override fun getPresenter(): P? {
        return this.mPresenter
    }

    override fun launchActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun killMyself() {
        activity.finish()
    }

    protected fun addDisposable(disposable: Disposable) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = CompositeDisposable()
        }
        this.compositeDisposable!!.add(disposable)
    }

    protected fun removeDisposable(disposable: Disposable) {
        if (this.compositeDisposable != null) {
            this.compositeDisposable!!.remove(disposable)
        }
    }
}
