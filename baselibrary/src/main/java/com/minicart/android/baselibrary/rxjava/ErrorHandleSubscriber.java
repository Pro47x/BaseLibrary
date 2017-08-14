package com.minicart.android.baselibrary.rxjava;

import android.support.annotation.NonNull;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class ErrorHandleSubscriber<T> implements Subscriber<T> {
    private ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler) {
        this.mHandlerFactory = rxErrorHandler.getHandlerFactory();
    }

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        mHandlerFactory.handleError(e);
    }
}