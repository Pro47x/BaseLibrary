package com.minicart.android.baselibrary.dagger2.delegate;


import android.content.Intent;
import android.os.Bundle;

import com.minicart.android.baselibrary.dagger2.component.AppComponent;
import com.minicart.android.baselibrary.mvp.IPresenter;
import com.minicart.android.baselibrary.mvp.IView;


/**
 * Created by jess on 26/04/2017 21:42
 * Contact with jess.yan.effort@gmail.com
 */

public interface IActivity<P extends IPresenter> extends IView<P> {
    void setupActivityComponent(AppComponent appComponent);

    boolean useFragment();

    /**
     * 初始化根布局ID
     *
     * @param savedInstanceState
     * @return
     */
    int initRootLayoutID(Bundle savedInstanceState);

    void initView(Bundle savedInstanceState);

    void initData(Bundle savedInstanceState);

    void initListener(Bundle savedInstanceState);

    void launchActivity(Intent intent);

    void killMyself();

}
