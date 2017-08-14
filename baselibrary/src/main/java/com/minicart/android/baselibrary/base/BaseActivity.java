package com.minicart.android.baselibrary.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.minicart.android.baselibrary.dagger2.component.AppComponent;
import com.minicart.android.baselibrary.dagger2.delegate.IActivity;
import com.minicart.android.baselibrary.mvp.IPresenter;

import javax.inject.Inject;

/**
 * @类名：BaseActivity
 * @描述：Activity的基类
 * @创建人：54506
 * @创建时间：2016/8/5 16:47
 * @版本：
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity<P> {
    protected String TAG = getClass().getSimpleName();
    private AtyHandler mHandler = new AtyHandler();
    static final int WHAT_CLOSE_PROGRESS_DIALOG = 0;

    @Inject
    protected P mPresenter;

    protected ProgressDialog mLoadDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean useFragment() {
        return false;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initListener(Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (getPresenter() != null) {
            getPresenter().onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().onResume();
        }
    }

    @Override
    protected void onPause() {
        if (getPresenter() != null) {
            getPresenter().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.cancel();
        }
        if (getPresenter() != null) {
            getPresenter().onStop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void hideLoading(long delayedMillisecond) {
        if (mLoadDialog != null) {
            Message message = mHandler.obtainMessage(WHAT_CLOSE_PROGRESS_DIALOG, mLoadDialog);
            mHandler.sendMessageDelayed(message, delayedMillisecond);
        }
    }

    @Override
    public void hideLoading() {
        hideLoading(0);
    }

    @Override
    public void showLoadingMessage(String message) {
        if (mLoadDialog == null) {
            mLoadDialog = new ProgressDialog(this);
        }
        mLoadDialog.setMessage(message);
        mLoadDialog.show();
    }

    @Override
    public void updateLoadingMessage(String msg) {
        if (mLoadDialog == null) {
            return;
        }
        if (mLoadDialog.isShowing()) {
            mLoadDialog.setMessage(msg);
        }
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onBackPressed() {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        super.onBackPressed();
    }

    static class AtyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CLOSE_PROGRESS_DIALOG:
                    ((ProgressDialog) msg.obj).cancel();
                    break;
            }
        }
    }
}











