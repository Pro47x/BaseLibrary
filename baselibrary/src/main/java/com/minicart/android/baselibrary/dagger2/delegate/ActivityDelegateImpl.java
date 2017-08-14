package com.minicart.android.baselibrary.dagger2.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.minicart.android.baselibrary.dagger2.App;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jess on 26/04/2017 20:23
 * Contact with jess.yan.effort@gmail.com
 */

public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;
    private Unbinder mUnbinder;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        } else {
            mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        iActivity.setupActivityComponent(((App) mActivity.getApplication()).getAppComponent());//依赖注入
        try {
            int layoutResID = iActivity.initRootLayoutID(savedInstanceState);
            if (layoutResID != 0) {
                mActivity.setContentView(layoutResID);
                mUnbinder = ButterKnife.bind(mActivity);
                iActivity.initView(savedInstanceState);
                iActivity.initData(savedInstanceState);
                iActivity.initListener(savedInstanceState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //绑定到butterknife
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        this.mUnbinder = null;
        this.iActivity = null;
        this.mActivity = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected ActivityDelegateImpl(Parcel in) {
        this.mActivity = in.readParcelable(Activity.class.getClassLoader());
        this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
        this.mUnbinder = in.readParcelable(Unbinder.class.getClassLoader());
    }

    public static final Creator<ActivityDelegateImpl> CREATOR = new Creator<ActivityDelegateImpl>() {
        @Override
        public ActivityDelegateImpl createFromParcel(Parcel source) {
            return new ActivityDelegateImpl(source);
        }

        @Override
        public ActivityDelegateImpl[] newArray(int size) {
            return new ActivityDelegateImpl[size];
        }
    };
}
