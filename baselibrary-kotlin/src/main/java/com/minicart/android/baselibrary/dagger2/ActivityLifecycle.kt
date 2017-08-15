package com.minicart.android.baselibrary.dagger2

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View

import com.minicart.android.baselibrary.dagger2.delegate.ActivityDelegate
import com.minicart.android.baselibrary.dagger2.delegate.ActivityDelegateImpl
import com.minicart.android.baselibrary.dagger2.delegate.FragmentDelegate
import com.minicart.android.baselibrary.dagger2.delegate.FragmentDelegateImpl
import com.minicart.android.baselibrary.dagger2.delegate.IActivity
import com.minicart.android.baselibrary.dagger2.delegate.IFragment
import com.minicart.android.baselibrary.support.AppManager
import com.minicart.android.baselibrary.support.LogUtil

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityLifecycle @Inject
constructor(private val mAppManager: AppManager, private val mApplication: Application) : Application.ActivityLifecycleCallbacks {
    private var mFragmentLifecycle: FragmentLifecycle? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        LogUtil.d(activity.javaClass.simpleName, "Created")
        mAppManager.pullActivity(activity)
        if (activity is IActivity<*> && activity.intent != null) {
            var activityDelegate = fetchActivityDelegate(activity)
            if (activityDelegate == null) {
                activityDelegate = ActivityDelegateImpl(activity)
                activity.intent.putExtra(ActivityDelegate.ACTIVITY_DELEGATE, activityDelegate)
            }
            activityDelegate.onCreate(savedInstanceState)

            val useFragment = (activity as IActivity<*>).useFragment()
            if (activity is FragmentActivity && useFragment) {
                if (mFragmentLifecycle == null) {
                    mFragmentLifecycle = FragmentLifecycle()
                }
                (activity as FragmentActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycle, true)
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onStart()
    }

    override fun onActivityResumed(activity: Activity) {
        mAppManager.currentActivity = activity

        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onPause()
    }

    override fun onActivityStopped(activity: Activity) {
        if (mAppManager.currentActivity === activity) {
            mAppManager.currentActivity = null
        }
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onStop()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        val activityDelegate = fetchActivityDelegate(activity)
        activityDelegate?.onSaveInstanceState(outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        mAppManager.removeActivity(activity)

        val activityDelegate = fetchActivityDelegate(activity)
        if (activityDelegate != null) {
            activityDelegate.onDestroy()
            activity.intent.removeExtra(ActivityDelegate.ACTIVITY_DELEGATE)
        }
    }

    private fun fetchActivityDelegate(activity: Activity): ActivityDelegate? {
        var activityDelegate: ActivityDelegate? = null
        if (activity is IActivity<*> && activity.intent != null) {
            activityDelegate = activity.intent.getParcelableExtra<ActivityDelegate>(ActivityDelegate.ACTIVITY_DELEGATE)
        }
        return activityDelegate
    }

    internal class FragmentLifecycle : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentAttached(fm: FragmentManager?, f: Fragment?, context: Context?) {
            super.onFragmentAttached(fm, f, context)
            if (f is IFragment && f.arguments != null) {
                var fragmentDelegate = fetchFragmentDelegate(f)
                if (fragmentDelegate == null || !fragmentDelegate.isAdded) {
                    fragmentDelegate = FragmentDelegateImpl(fm!!, f)
                    f.arguments.putParcelable(FragmentDelegate.FRAGMENT_DELEGATE, fragmentDelegate)
                }
                fragmentDelegate.onAttach(context!!)
            }
        }

        override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onCreate(savedInstanceState!!)
        }

        override fun onFragmentViewCreated(fm: FragmentManager?, f: Fragment?, v: View?, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onCreateView(v!!, savedInstanceState!!)
        }

        override fun onFragmentActivityCreated(fm: FragmentManager?, f: Fragment?, savedInstanceState: Bundle?) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onActivityCreate(savedInstanceState!!)
        }

        override fun onFragmentStarted(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentStarted(fm, f)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onStart()
        }

        override fun onFragmentResumed(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentResumed(fm, f)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onResume()
        }

        override fun onFragmentPaused(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentPaused(fm, f)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onPause()
        }

        override fun onFragmentStopped(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentStopped(fm, f)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onStop()
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentViewDestroyed(fm, f)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onDestroyView()
        }

        override fun onFragmentDestroyed(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentDestroyed(fm, f)
            val fragmentDelegate = fetchFragmentDelegate(f)
            fragmentDelegate?.onDestroy()
        }

        override fun onFragmentDetached(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentDetached(fm, f)
            val fragmentDelegate = fetchFragmentDelegate(f)
            if (fragmentDelegate != null) {
                fragmentDelegate.onDetach()
                f!!.arguments.clear()
            }
        }


        private fun fetchFragmentDelegate(fragment: Fragment): FragmentDelegate? {
            if (fragment is IFragment) {
                return if (fragment.arguments == null) null else fragment.arguments.getParcelable<Parcelable>(FragmentDelegate.FRAGMENT_DELEGATE)
            }
            return null
        }
    }

}