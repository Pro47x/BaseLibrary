package com.minicart.android.baselibrary.mvp;

import android.content.Context;
import android.content.Intent;

/**
 * @类名：IView
 * @描述：
 * @创建人：54506
 * @创建时间：2017/1/13 11:04
 * @版本：
 */

public interface IView<P extends IPresenter> {

    P getPresenter();

    Context getContext();

    void showLoadingMessage(String message);

    void updateLoadingMessage(String msg);

    void hideLoading();

    void hideLoading(long delayedMillisecond);

    void launchActivity(Intent intent);

    void killMyself();
}
