package com.minicart.android.baselibrary.base;


import android.content.Context;

import com.minicart.android.baselibrary.mvp.IModel;
import com.minicart.android.baselibrary.mvp.IPresenter;
import com.minicart.android.baselibrary.mvp.IView;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @类名：BasePresenter
 * @描述：控制器的基类
 * @创建人：54506
 * @创建时间：2016/9/18 11:31
 * @版本：
 */
public abstract class BasePresenter<V extends IView, M extends IModel> implements IPresenter<V> {
    private static Set<Integer> action = new HashSet<>();
    protected final String TAG = getClass().getSimpleName();

    private CompositeDisposable compositeDisposable;
    private CompositeDisposable mRxBusDisposable;

    private V mView;
    private M mModel;

    public BasePresenter() {
    }

    public BasePresenter(M model) {
        this.mModel = model;
    }

    public BasePresenter(V view, M model) {
        this.mView = view;
        this.mModel = model;
    }

    @Override
    public void injectView(V view) {
        this.mView = view;
        onAttachView();
    }

    @Override
    public void onAttachView() {

    }

    protected String getString(int resId) {
        return getView().getContext().getString(resId);
    }

    protected String getString(int resId, Object... formatArgs) {
        return getView().getContext().getString(resId, formatArgs);
    }

    @SuppressWarnings("unchecked")
    protected static <T> T cast(Object obj) {
        return (T) obj;
    }

    public V getView() {
        return mView;
    }

    public M getModel() {
        return mModel;
    }

    public Context getContext() {
        return getView().getContext();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

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
    public void onDestroy() {
        if (this.compositeDisposable != null) {
            if (!this.compositeDisposable.isDisposed()) {
                this.compositeDisposable.clear();
            }
            this.compositeDisposable = null;
        }
        if (this.mRxBusDisposable != null) {
            if (!this.mRxBusDisposable.isDisposed()) {
                this.mRxBusDisposable.clear();
            }
            this.mRxBusDisposable = null;
        }
        this.mView = null;
        this.mModel = null;
    }

    protected void addDisposable(Disposable disposable) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        this.compositeDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        if (this.compositeDisposable != null) {
            if (disposable != null) {
                this.compositeDisposable.remove(disposable);
                disposable.dispose();
            }
        }
    }

    protected void actionBegin(int requestCode) {
        if (action == null) {
            action = new HashSet<>();
        }
        action.add(requestCode);
    }

    protected void actionEnd(int requestCode) {
        if (action != null) {
            action.remove(requestCode);
        }
    }

    protected boolean actionGoing(int requestCode) {
        return action != null && action.contains(requestCode);
    }

    protected void registerRxBus(Disposable disposable) {
        if (mRxBusDisposable == null) {
            mRxBusDisposable = new CompositeDisposable();
        }
        mRxBusDisposable.add(disposable);
    }
}
