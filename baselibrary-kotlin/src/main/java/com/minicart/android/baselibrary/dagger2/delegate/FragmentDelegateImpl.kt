package com.minicart.android.baselibrary.dagger2.delegate

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder
import com.minicart.android.baselibrary.dagger2.App

/**
 * Created by jess on 29/04/2017 16:12
 * Contact with jess.yan.effort@gmail.com
 */

class FragmentDelegateImpl : FragmentDelegate {
    private var mFragmentManager: FragmentManager? = null
    private var mFragment: Fragment? = null
    private var iFragment: IFragment? = null
    private var mUnbinder: Unbinder? = null


    constructor(fragmentManager: FragmentManager, fragment: Fragment) {
        this.mFragmentManager = fragmentManager
        this.mFragment = fragment
        this.iFragment = fragment as IFragment
    }

    override fun onAttach(context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle) {
        iFragment!!.setupFragmentComponent((mFragment!!.activity.application as App).appComponent)
    }

    override fun onCreateView(view: View, savedInstanceState: Bundle) {
        //绑定到butterknife
        if (view != null) {
            mUnbinder = ButterKnife.bind(mFragment!!, view)
        }
    }

    override fun onActivityCreate(savedInstanceState: Bundle) {
        iFragment!!.initView(savedInstanceState)
        iFragment!!.initData(savedInstanceState)
        iFragment!!.initListener(savedInstanceState)
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

    override fun onDestroyView() {
        if (mUnbinder != null && mUnbinder !== Unbinder.EMPTY) {
            try {
                mUnbinder!!.unbind()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                //fix Bindings already cleared
            }

        }
    }

    override fun onDestroy() {
        this.mUnbinder = null
        this.mFragmentManager = null
        this.mFragment = null
        this.iFragment = null
    }

    override fun onDetach() {

    }

    /**
     * Return true if the fragment is currently added to its activity.
     */
    override val isAdded: Boolean
        get() = if (mFragment == null) false else mFragment!!.isAdded

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

    }

    protected constructor(parcel: Parcel) {
        this.mFragmentManager = parcel.readParcelable<Parcelable>(FragmentManager::class.java.classLoader) as FragmentManager
        this.mFragment = parcel.readParcelable<Parcelable>(Fragment::class.java.classLoader) as Fragment
        this.iFragment = parcel.readParcelable<Parcelable>(IFragment::class.java.classLoader) as IFragment
        this.mUnbinder = parcel.readParcelable<Parcelable>(Unbinder::class.java.classLoader) as Unbinder
    }

    companion object {
        private val TAG = "FragmentDelegateImpl"

        val CREATOR: Parcelable.Creator<FragmentDelegateImpl> = object : Parcelable.Creator<FragmentDelegateImpl> {
            override fun createFromParcel(source: Parcel): FragmentDelegateImpl {
                return FragmentDelegateImpl(source)
            }

            override fun newArray(size: Int): Array<FragmentDelegateImpl?> {
                return arrayOfNulls(size)
            }
        }
    }
}
