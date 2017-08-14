package com.minicart.android.baselibrary.mvp;

/**
 * @类名：IPresenter
 * @描述：
 * @创建人：54506
 * @创建时间：2017/1/13 11:04
 * @版本：
 */

public interface IPresenter<V extends IView> {

    String SORT_TYPE = "01";
    String PAGE_SIZE = "2000";
    String CURRENT_PAGE = "1";

    void injectView(V view);

    void onAttachView();

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

}
