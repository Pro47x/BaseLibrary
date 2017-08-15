package com.minicart.android.baselibrary.base

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity

import com.minicart.android.baselibrary.dagger2.component.AppComponent
import com.minicart.android.baselibrary.dagger2.delegate.IActivity
import com.minicart.android.baselibrary.mvp.IPresenter

import javax.inject.Inject

/**
 * @类名：BaseActivity
 * *
 * @描述：Activity的基类
 * *
 * @创建人：54506
 * *
 * @创建时间：2016/8/5 16:47
 * *
 * @版本：
 */
abstract class BaseActivity<P : IPresenter<*>> : AppCompatActivity(), IActivity<P> {
    protected var TAG = javaClass.simpleName
    private val mHandler = AtyHandler()

    @Inject
    protected var mPresenter: P? = null

    protected var mLoadDialog: ProgressDialog? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun getContext(): Context {
        return this
    }

    override fun useFragment(): Boolean {
        return false
    }

    override fun initView(savedInstanceState: Bundle) {

    }

    override fun initData(savedInstanceState: Bundle) {

    }

    override fun initListener(savedInstanceState: Bundle) {

    }

    override fun onStart() {
        super.onStart()
        if (getPresenter() != null) {
            getPresenter()!!.onStart()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (getPresenter() != null) {
            getPresenter()!!.onRestart()
        }
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
        if (mLoadDialog != null && mLoadDialog!!.isShowing) {
            mLoadDialog!!.cancel()
        }
        if (getPresenter() != null) {
            getPresenter()!!.onStop()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (mPresenter != null) {
            mPresenter!!.onDestroy()
            mPresenter = null
        }
        super.onDestroy()
    }

    override fun hideLoading(delayedMillisecond: Long) {
        if (mLoadDialog != null) {
            val message = mHandler.obtainMessage(WHAT_CLOSE_PROGRESS_DIALOG, mLoadDialog)
            mHandler.sendMessageDelayed(message, delayedMillisecond)
        }
    }

    override fun hideLoading() {
        hideLoading(0)
    }

    override fun showLoadingMessage(message: String) {
        if (mLoadDialog == null) {
            mLoadDialog = ProgressDialog(this)
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

    override fun launchActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }

    override fun getPresenter(): P? {
        return mPresenter
    }

    override fun onBackPressed() {
        if (mLoadDialog != null && mLoadDialog!!.isShowing) {
            mLoadDialog!!.dismiss()
        }
        super.onBackPressed()
    }

    internal class AtyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                WHAT_CLOSE_PROGRESS_DIALOG -> (msg.obj as ProgressDialog).cancel()
            }
        }
    }

    companion object {
        internal val WHAT_CLOSE_PROGRESS_DIALOG = 0
    }
}











