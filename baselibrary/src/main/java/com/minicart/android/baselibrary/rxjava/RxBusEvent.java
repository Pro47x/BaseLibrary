package com.minicart.android.baselibrary.rxjava;

/**
 * @类名：RxBusEvent
 * @描述：
 * @创建人：54506
 * @创建时间：2017/4/13 16:47
 * @版本：
 */

public class RxBusEvent implements IRxBusEvent {
    private final boolean success;

    private final Throwable throwable;

    public RxBusEvent() {
        this(null);
    }

    public RxBusEvent(Throwable throwable) {
        this.throwable = throwable;
        this.success = throwable == null;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable throwable() {
        return throwable;
    }
}
