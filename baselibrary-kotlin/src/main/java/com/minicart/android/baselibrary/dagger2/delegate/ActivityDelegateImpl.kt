package com.minicart.android.baselibrary.dagger2.delegate

import android.app.Activity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.view.Window
import butterknife.ButterKnife
import butterknife.Unbinder
import com.minicart.android.baselibrary.dagger2.App

/**
 * Created by jess on 26/04/2017 20:23
 * Contact with jess.yan.effort@gmail.com
 */

class ActivityDelegateImpl : ActivityDelegate {
    private var mActivity: Activity? = null
    private var iActivity: IActivity<*>? = null
    private var mUnbinder: Unbinder? = null

    constructor(activity: Activity) {
        this.mActivity = activity
        this.iActivity = activity as IActivity<*>
    }

    override fun onCreate(savedInstanceState: Bundle) {
        var a1: Activity = mActivity!!
        if (a1 is AppCompatActivity) {
            a1.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        } else {
            a1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        var a2: IActivity<*> = iActivity!!

        a2.setupActivityComponent((a1.application as App).getAppComponent())//依赖注入
        try {
            val layoutResID = a2.initRootLayoutID(savedInstanceState)
            if (layoutResID != 0) {
                a1.setContentView(layoutResID)
                mUnbinder = ButterKnife.bind(a1)
                a2.initView(savedInstanceState)
                a2.initData(savedInstanceState)
                a2.initListener(savedInstanceState)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //绑定到butterknife
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {
        if (mUnbinder != null && mUnbinder !== Unbinder.EMPTY) {
            mUnbinder!!.unbind()
        }
        this.mUnbinder = null
        this.iActivity = null
        this.mActivity = null
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

    }

    protected constructor(parcel: Parcel) {
        this.mActivity = parcel.readParcelable<Parcelable>(Activity::class.java.classLoader) as Activity
        this.iActivity = parcel.readParcelable<Parcelable>(IActivity::class.java.classLoader) as IActivity<*>
        this.mUnbinder = parcel.readParcelable<Parcelable>(Unbinder::class.java.classLoader) as Unbinder
    }

    companion object {

        val CREATOR: Parcelable.Creator<ActivityDelegateImpl> = object : Parcelable.Creator<ActivityDelegateImpl> {
            override fun createFromParcel(source: Parcel): ActivityDelegateImpl {
                return ActivityDelegateImpl(source)
            }

            override fun newArray(size: Int): Array<ActivityDelegateImpl?> {
                return arrayOfNulls(size)
            }
        }
    }
}
