package com.minicart.android.baselibrary.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minicart.android.baselibrary.dagger2.component.AppComponent;
import com.minicart.android.baselibrary.dagger2.delegate.IFragment;
import com.minicart.android.baselibrary.mvp.IPresenter;
import com.minicart.android.baselibrary.mvp.IView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @类名：BaseFragment
 * @描述：Fragment的基类
 * @创建人：54506
 * @创建时间：2016/8/5 17:47
 * @版本：
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, IView<P> {
    protected LayoutInflater inflater;
    protected ViewGroup parent;
    protected final String TAG = getClass().getSimpleName();
    @Inject
    protected P mPresenter;
    private CompositeDisposable compositeDisposable;

    protected View mRootView;

    private BaseActivity.AtyHandler mHandler = new BaseActivity.AtyHandler();
    protected ProgressDialog mLoadDialog;

    public BaseFragment() {
        //必须确保在Fragment实例化时setArguments()
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化布局
        this.inflater = inflater;
        parent = container;
        int layoutID = initRootLayoutID(inflater, container, savedInstanceState);
        if (layoutID != 0) {
            mRootView = inflater.inflate(layoutID, container, false);
        }
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().onStart();
        }
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

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
    public void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().onResume();
        }
    }

    @Override
    public void onPause() {
        if (getPresenter() != null) {
            getPresenter().onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (getPresenter() != null) {
            getPresenter().onStop();
        }
        super.onStop();
    }

    public void onBackPressed() {

    }

    @Override
    public void onDestroyView() {
        if (this.compositeDisposable != null) {
            if (!this.compositeDisposable.isDisposed()) {
                this.compositeDisposable.clear();
            }
            this.compositeDisposable = null;
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected View makeView(int rsId) {
        return inflater.inflate(rsId, parent, false);
    }

    @Override
    public void hideLoading(long delayedMillisecond) {
        if (mLoadDialog != null) {
            Message message = mHandler.obtainMessage(BaseActivity.WHAT_CLOSE_PROGRESS_DIALOG, mLoadDialog);
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
            mLoadDialog = new ProgressDialog(getContext());
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
    public P getPresenter() {
        return this.mPresenter;
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void killMyself() {
        getActivity().finish();
    }

    protected void addDisposable(Disposable disposable) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        this.compositeDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        if (this.compositeDisposable != null) {
            this.compositeDisposable.remove(disposable);
        }
    }
}
