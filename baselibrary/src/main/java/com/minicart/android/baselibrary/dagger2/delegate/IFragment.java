package com.minicart.android.baselibrary.dagger2.delegate;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.minicart.android.baselibrary.dagger2.component.AppComponent;


/**
 * Created by jess on 26/04/2017 21:42
 * Contact with jess.yan.effort@gmail.com
 */

public interface IFragment {
    void setupFragmentComponent(AppComponent appComponent);

    /**
     * 初始化根布局ID
     *
     * @param savedInstanceState
     * @return
     */
    int initRootLayoutID(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void initView(Bundle savedInstanceState);

    void initData(Bundle savedInstanceState);

    void initListener(Bundle savedInstanceState);

    void launchActivity(Intent intent);

    void killMyself();
}
